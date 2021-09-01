/*********************** From the file "report.c" **************************/
/*
** Procedures for generating reports and tables in the LEMON parser generator.
*/

/* Generate a filename with the given suffix.  Space to hold the
** name comes from malloc() and must be freed by the calling
** function.
*/
PRIVATE char *file_makename(lemp,suffix)
struct lemon *lemp;
char *suffix;
{
  char *name;
  char *cp;

  name = malloc( strlen(lemp->filename) + strlen(suffix) + 5 );
  if( name==0 ){
    fprintf(stderr,"Can't allocate space for a filename.\n");
    exit(1);
  }
  strcpy(name,lemp->filename);
  cp = strrchr(name,'.');
  if( cp ) *cp = 0;
  strcat(name,suffix);
  return name;
}

/* Open a file with a name based on the name of the input file,
** but with a different (specified) suffix, and return a pointer
** to the stream */
PRIVATE FILE *file_open(lemp,suffix,mode)
struct lemon *lemp;
char *suffix;
char *mode;
{
  FILE *fp;

  if( lemp->outname ) free(lemp->outname);
  lemp->outname = file_makename(lemp, suffix);
  fp = fopen(lemp->outname,mode);
  if( fp==0 && *mode=='w' ){
    fprintf(stderr,"Can't open file \"%s\".\n",lemp->outname);
    lemp->errorcnt++;
    return 0;
  }
  return fp;
}

/* Duplicate the input file without comments and without actions 
** on rules */
void Reprint(lemp)
struct lemon *lemp;
{
  struct rule *rp;
  struct symbol *sp;
  int i, j, maxlen, len, ncolumns, skip;
  printf("// Reprint of input file \"%s\".\n// Symbols:\n",lemp->filename);
  maxlen = 10;
  for(i=0; i<lemp->nsymbol; i++){
    sp = lemp->symbols[i];
    len = strlen(sp->name);
    if( len>maxlen ) maxlen = len;
  }
  ncolumns = 76/(maxlen+5);
  if( ncolumns<1 ) ncolumns = 1;
  skip = (lemp->nsymbol + ncolumns - 1)/ncolumns;
  for(i=0; i<skip; i++){
    printf("//");
    for(j=i; j<lemp->nsymbol; j+=skip){
      sp = lemp->symbols[j];
      assert( sp->index==j );
      printf(" %3d %-*.*s",j,maxlen,maxlen,sp->name);
    }
    printf("\n");
  }
  for(rp=lemp->rule; rp; rp=rp->next){
    printf("%s",rp->lhs->name);
/*    if( rp->lhsalias ) printf("(%s)",rp->lhsalias); */
    printf(" ::=");
    for(i=0; i<rp->nrhs; i++){
      printf(" %s",rp->rhs[i]->name);
/*      if( rp->rhsalias[i] ) printf("(%s)",rp->rhsalias[i]); */
    }
    printf(".");
    if( rp->precsym ) printf(" [%s]",rp->precsym->name);
/*    if( rp->code ) printf("\n    %s",rp->code); */
    printf("\n");
  }
}

void ConfigPrint(fp,cfp)
FILE *fp;
struct config *cfp;
{
  struct rule *rp;
  int i;
  rp = cfp->rp;
  fprintf(fp,"%s ::=",rp->lhs->name);
  for(i=0; i<=rp->nrhs; i++){
    if( i==cfp->dot ) fprintf(fp," *");
    if( i==rp->nrhs ) break;
    fprintf(fp," %s",rp->rhs[i]->name);
  }
}

/* #define TEST */
#ifdef TEST
/* Print a set */
PRIVATE void SetPrint(out,set,lemp)
FILE *out;
char *set;
struct lemon *lemp;
{
  int i;
  char *spacer;
  spacer = "";
  fprintf(out,"%12s[","");
  for(i=0; i<lemp->nterminal; i++){
    if( SetFind(set,i) ){
      fprintf(out,"%s%s",spacer,lemp->symbols[i]->name);
      spacer = " ";
    }
  }
  fprintf(out,"]\n");
}

/* Print a plink chain */
PRIVATE void PlinkPrint(out,plp,tag)
FILE *out;
struct plink *plp;
char *tag;
{
  while( plp ){
    fprintf(out,"%12s%s (state %2d) ","",tag,plp->cfp->stp->index);
    ConfigPrint(out,plp->cfp);
    fprintf(out,"\n");
    plp = plp->next;
  }
}
#endif

/* Print an action to the given file descriptor.  Return FALSE if
** nothing was actually printed.
*/
int PrintAction(struct action *ap, FILE *fp, int indent){
  int result = 1;
  switch( ap->type ){
    case SHIFT:
      fprintf(fp,"%*s shift  %d",indent,ap->sp->name,ap->x.stp->index);
      break;
    case REDUCE:
      fprintf(fp,"%*s reduce %d",indent,ap->sp->name,ap->x.rp->index);
      break;
    case ACCEPT:
      fprintf(fp,"%*s accept",indent,ap->sp->name);
      break;
    case ERROR:
      fprintf(fp,"%*s error",indent,ap->sp->name);
      break;
    case CONFLICT:
      fprintf(fp,"%*s reduce %-3d ** Parsing conflict **",
        indent,ap->sp->name,ap->x.rp->index);
      break;
    case SH_RESOLVED:
    case RD_RESOLVED:
    case NOT_USED:
      result = 0;
      break;
  }
  return result;
}

/* Generate the "y.output" log file */
void ReportOutput(lemp)
struct lemon *lemp;
{
  int i;
  struct state *stp;
  struct config *cfp;
  struct action *ap;
  FILE *fp;

  fp = file_open(lemp,".out","w");
  if( fp==0 ) return;
  fprintf(fp," \b");
  for(i=0; i<lemp->nstate; i++){
    stp = lemp->sorted[i];
    fprintf(fp,"State %d:\n",stp->index);
    if( lemp->basisflag ) cfp=stp->bp;
    else                  cfp=stp->cfp;
    while( cfp ){
      char buf[20];
      if( cfp->dot==cfp->rp->nrhs ){
        sprintf(buf,"(%d)",cfp->rp->index);
        fprintf(fp,"    %5s ",buf);
      }else{
        fprintf(fp,"          ");
      }
      ConfigPrint(fp,cfp);
      fprintf(fp,"\n");
#ifdef TEST
      SetPrint(fp,cfp->fws,lemp);
      PlinkPrint(fp,cfp->fplp,"To  ");
      PlinkPrint(fp,cfp->bplp,"From");
#endif
      if( lemp->basisflag ) cfp=cfp->bp;
      else                  cfp=cfp->next;
    }
    fprintf(fp,"\n");
    for(ap=stp->ap; ap; ap=ap->next){
      if( PrintAction(ap,fp,30) ) fprintf(fp,"\n");
    }
    fprintf(fp,"\n");
  }
  fclose(fp);
  return;
}

/* Search for the file "name" which is in the same directory as
** the exacutable */
PRIVATE char *pathsearch(argv0,name,modemask)
char *argv0;
char *name;
int modemask;
{
  char *pathlist;
  char *path,*cp;
  char c;
  extern int access();

#ifdef __WIN32__
  cp = strrchr(argv0,'\\');
#else
  cp = strrchr(argv0,'/');
#endif
  if( cp ){
    c = *cp;
    *cp = 0;
    path = (char *)malloc( strlen(argv0) + strlen(name) + 2 );
    if( path ) sprintf(path,"%s/%s",argv0,name);
    *cp = c;
  }else{
    extern char *getenv();
    pathlist = getenv("PATH");
    if( pathlist==0 ) pathlist = ".:/bin:/usr/bin";
    path = (char *)malloc( strlen(pathlist)+strlen(name)+2 );
    if( path!=0 ){
      while( *pathlist ){
        cp = strchr(pathlist,':');
        if( cp==0 ) cp = &pathlist[strlen(pathlist)];
        c = *cp;
        *cp = 0;
        sprintf(path,"%s/%s",pathlist,name);
        *cp = c;
        if( c==0 ) pathlist = "";
        else pathlist = &cp[1];
        if( access(path,modemask)==0 ) break;
      }
    }
  }
  return path;
}

/* Given an action, compute the integer value for that action
** which is to be put in the action table of the generated machine.
** Return negative if no action should be generated.
*/
PRIVATE int compute_action(lemp,ap)
struct lemon *lemp;
struct action *ap;
{
  int act;
  switch( ap->type ){
    case SHIFT:  act = ap->x.stp->index;               break;
    case REDUCE: act = ap->x.rp->index + lemp->nstate; break;
    case ERROR:  act = lemp->nstate + lemp->nrule;     break;
    case ACCEPT: act = lemp->nstate + lemp->nrule + 1; break;
    default:     act = -1; break;
  }
  return act;
}

#define LINESIZE 1000
/* The next cluster of routines are for reading the template file
** and writing the results to the generated parser */
/* The first function transfers data from "in" to "out" until
** a line is seen which begins with "%%".  The line number is
** tracked.
**
** if name!=0, then any word that begin with "Parse" is changed to
** begin with *name instead.
*/
PRIVATE void tplt_xfer(name,in,out,lineno)
char *name;
FILE *in;
FILE *out;
int *lineno;
{
  int i, iStart;
  char line[LINESIZE];
  while( fgets(line,LINESIZE,in) && (line[0]!='%' || line[1]!='%') ){
    (*lineno)++;
    iStart = 0;
    if( name ){
      for(i=0; line[i]; i++){
        if( line[i]=='P' && strncmp(&line[i],"Parse",5)==0
          && (i==0 || !isalpha(line[i-1]))
        ){
          if( i>iStart ) fprintf(out,"%.*s",i-iStart,&line[iStart]);
          fprintf(out,"%s",name);
          i += 4;
          iStart = i+1;
        }
      }
    }
    fprintf(out,"%s",&line[iStart]);
  }
}

/* The next function finds the template file and opens it, returning
** a pointer to the opened file. */
PRIVATE FILE *tplt_open(lemp)
struct lemon *lemp;
{
  static char templatename[] = "lempar.c";
  char buf[1000];
  FILE *in;
  char *tpltname;
  char *cp;

  cp = strrchr(lemp->filename,'.');
  if( cp ){
    sprintf(buf,"%.*s.lt",(int)cp-(int)lemp->filename,lemp->filename);
  }else{
    sprintf(buf,"%s.lt",lemp->filename);
  }
  if( access(buf,004)==0 ){
    tpltname = buf;
  }else{
    tpltname = pathsearch(lemp->argv0,templatename,0);
  }
  if( tpltname==0 ){
    fprintf(stderr,"Can't find the parser driver template file \"%s\".\n",
    templatename);
    lemp->errorcnt++;
    return 0;
  }
  in = fopen(tpltname,"r");
  if( in==0 ){
    fprintf(stderr,"Can't open the template file \"%s\".\n",templatename);
    lemp->errorcnt++;
    return 0;
  }
  return in;
}

/* Print a string to the file and keep the linenumber up to date */
PRIVATE void tplt_print(out,lemp,str,strln,lineno)
FILE *out;
struct lemon *lemp;
char *str;
int strln;
int *lineno;
{
  if( str==0 ) return;
  fprintf(out,"#line %d \"%s\"\n",strln,lemp->filename); (*lineno)++;
  while( *str ){
    if( *str=='\n' ) (*lineno)++;
    putc(*str,out);
    str++;
  }
  fprintf(out,"\n#line %d \"%s\"\n",*lineno+2,lemp->outname); (*lineno)+=2;
  return;
}

/*
** The following routine emits code for the destructor for the
** symbol sp
*/
void emit_destructor_code(out,sp,lemp,lineno)
FILE *out;
struct symbol *sp;
struct lemon *lemp;
int *lineno;
{
 char *cp;

 int linecnt = 0;
 if( sp->type==TERMINAL ){
   cp = lemp->tokendest;
   if( cp==0 ) return;
   fprintf(out,"#line %d \"%s\"\n{",lemp->tokendestln,lemp->filename);
 }else{
   cp = sp->destructor;
   if( cp==0 ) return;
   fprintf(out,"#line %d \"%s\"\n{",sp->destructorln,lemp->filename);
 }
 for(; *cp; cp++){
   if( *cp=='$' && cp[1]=='$' ){
     fprintf(out,"(yypminor->yy%d)",sp->dtnum);
     cp++;
     continue;
   }
   if( *cp=='\n' ) linecnt++;
   fputc(*cp,out);
 }
 (*lineno) += 3 + linecnt;
 fprintf(out,"}\n#line %d \"%s\"\n",*lineno,lemp->outname);
 return;
}

/*
** Return TRUE (non-zero) if the given symbol has a distructor.
*/
int has_destructor(sp, lemp)
struct symbol *sp;
struct lemon *lemp;
{
  int ret;
  if( sp->type==TERMINAL ){
    ret = lemp->tokendest!=0;
  }else{
    ret = sp->destructor!=0;
  }
  return ret;
}

/* 
** Generate code which executes when the rule "rp" is reduced.  Write
** the code to "out".  Make sure lineno stays up-to-date.
*/
PRIVATE void emit_code(out,rp,lemp,lineno)
FILE *out;
struct rule *rp;
struct lemon *lemp;
int *lineno;
{
 char *cp, *xp;
 int linecnt = 0;
 int i;
 char lhsused = 0;    /* True if the LHS element has been used */
 char used[MAXRHS];   /* True for each RHS element which is used */

 for(i=0; i<rp->nrhs; i++) used[i] = 0;
 lhsused = 0;

 /* Generate code to do the reduce action */
 if( rp->code ){
   fprintf(out,"#line %d \"%s\"\n{",rp->line,lemp->filename);
   for(cp=rp->code; *cp; cp++){
     if( isalpha(*cp) && (cp==rp->code || !isalnum(cp[-1])) ){
       char saved;
       for(xp= &cp[1]; isalnum(*xp); xp++);
       saved = *xp;
       *xp = 0;
       if( rp->lhsalias && strcmp(cp,rp->lhsalias)==0 ){
         fprintf(out,"yygotominor.yy%d",rp->lhs->dtnum);
         cp = xp;
         lhsused = 1;
       }else{
         for(i=0; i<rp->nrhs; i++){
           if( rp->rhsalias[i] && strcmp(cp,rp->rhsalias[i])==0 ){
             fprintf(out,"yymsp[%d].minor.yy%d",i-rp->nrhs+1,rp->rhs[i]->dtnum);
             cp = xp;
             used[i] = 1;
             break;
           }
         }
       }
       *xp = saved;
     }
     if( *cp=='\n' ) linecnt++;
     fputc(*cp,out);
   } /* End loop */
   (*lineno) += 3 + linecnt;
   fprintf(out,"}\n#line %d \"%s\"\n",*lineno,lemp->outname);
 } /* End if( rp->code ) */

 /* Check to make sure the LHS has been used */
 if( rp->lhsalias && !lhsused ){
   ErrorMsg(lemp->filename,rp->ruleline,
     "Label \"%s\" for \"%s(%s)\" is never used.",
       rp->lhsalias,rp->lhs->name,rp->lhsalias);
   lemp->errorcnt++;
 }

 /* Generate destructor code for RHS symbols which are not used in the
 ** reduce code */
 for(i=0; i<rp->nrhs; i++){
   if( rp->rhsalias[i] && !used[i] ){
     ErrorMsg(lemp->filename,rp->ruleline,
       "Label $%s$ for \"%s(%s)\" is never used.",
       rp->rhsalias[i],rp->rhs[i]->name,rp->rhsalias[i]);
     lemp->errorcnt++;
   }else if( rp->rhsalias[i]==0 ){
     if( has_destructor(rp->rhs[i],lemp) ){
       fprintf(out,"  yy_destructor(%d,&yymsp[%d].minor);\n",
          rp->rhs[i]->index,i-rp->nrhs+1); (*lineno)++;
     }else{
       fprintf(out,"        /* No destructor defined for %s */\n",
        rp->rhs[i]->name);
        (*lineno)++;
     }
   }
 }
 return;
}

/*
** Print the definition of the union used for the parser's data stack.
** This union contains fields for every possible data type for tokens
** and nonterminals.  In the process of computing and printing this
** union, also set the ".dtnum" field of every terminal and nonterminal
** symbol.
*/
void print_stack_union(out,lemp,plineno,mhflag)
FILE *out;                  /* The output stream */
struct lemon *lemp;         /* The main info structure for this parser */
int *plineno;               /* Pointer to the line number */
int mhflag;                 /* True if generating makeheaders output */
{
  int lineno = *plineno;    /* The line number of the output */
  char **types;             /* A hash table of datatypes */
  int arraysize;            /* Size of the "types" array */
  int maxdtlength;          /* Maximum length of any ".datatype" field. */
  char *stddt;              /* Standardized name for a datatype */
  int i,j;                  /* Loop counters */
  int hash;                 /* For hashing the name of a type */
  char *name;               /* Name of the parser */

  /* Allocate and initialize types[] and allocate stddt[] */
  arraysize = lemp->nsymbol * 2;
  types = (char**)malloc( arraysize * sizeof(char*) );
  for(i=0; i<arraysize; i++) types[i] = 0;
  maxdtlength = 0;
  for(i=0; i<lemp->nsymbol; i++){
    int len;
    struct symbol *sp = lemp->symbols[i];
    if( sp->datatype==0 ) continue;
    len = strlen(sp->datatype);
    if( len>maxdtlength ) maxdtlength = len;
  }
  stddt = (char*)malloc( maxdtlength*2 + 1 );
  if( types==0 || stddt==0 ){
    fprintf(stderr,"Out of memory.\n");
    exit(1);
  }

  /* Build a hash table of datatypes. The ".dtnum" field of each symbol
  ** is filled in with the hash index plus 1.  A ".dtnum" value of 0 is
  ** used for terminal symbols and for nonterminals which don't specify
  ** a datatype using the %type directive. */
  for(i=0; i<lemp->nsymbol; i++){
    struct symbol *sp = lemp->symbols[i];
    char *cp;
    if( sp==lemp->errsym ){
      sp->dtnum = arraysize+1;
      continue;
    }
    if( sp->type!=NONTERMINAL || sp->datatype==0 ){
      sp->dtnum = 0;
      continue;
    }
    cp = sp->datatype;
    j = 0;
    while( isspace(*cp) ) cp++;
    while( *cp ) stddt[j++] = *cp++;
    while( j>0 && isspace(stddt[j-1]) ) j--;
    stddt[j] = 0;
    hash = 0;
    for(j=0; stddt[j]; j++){
      hash = hash*53 + stddt[j];
    }
    if( hash<0 ) hash = -hash;
    hash = hash%arraysize;
    while( types[hash] ){
      if( strcmp(types[hash],stddt)==0 ){
        sp->dtnum = hash + 1;
        break;
      }
      hash++;
      if( hash>=arraysize ) hash = 0;
    }
    if( types[hash]==0 ){
      sp->dtnum = hash + 1;
      types[hash] = (char*)malloc( strlen(stddt)+1 );
      if( types[hash]==0 ){
        fprintf(stderr,"Out of memory.\n");
        exit(1);
      }
      strcpy(types[hash],stddt);
    }
  }

  /* Print out the definition of YYTOKENTYPE and YYMINORTYPE */
  name = lemp->name ? lemp->name : "Parse";
  lineno = *plineno;
  if( mhflag ){ fprintf(out,"#if INTERFACE\n"); lineno++; }
  fprintf(out,"#define %sTOKENTYPE %s\n",name,
    lemp->tokentype?lemp->tokentype:"void*");  lineno++;
  if( mhflag ){ fprintf(out,"#endif\n"); lineno++; }
  fprintf(out,"typedef union {\n"); lineno++;
  fprintf(out,"  %sTOKENTYPE yy0;\n",name); lineno++;
  for(i=0; i<arraysize; i++){
    if( types[i]==0 ) continue;
    fprintf(out,"  %s yy%d;\n",types[i],i+1); lineno++;
    free(types[i]);
  }
  fprintf(out,"  int yy%d;\n",lemp->errsym->dtnum); lineno++;
  free(stddt);
  free(types);
  fprintf(out,"} YYMINORTYPE;\n"); lineno++;
  *plineno = lineno;
}

/* Generate C source code for the parser */
void ReportTable(lemp, mhflag)
struct lemon *lemp;
int mhflag;     /* Output in makeheaders format if true */
{
  FILE *out, *in;
  char line[LINESIZE];
  int  lineno;
  struct state *stp;
  struct action *ap;
  struct rule *rp;
  int i;
  int tablecnt;
  char *name;

  in = tplt_open(lemp);
  if( in==0 ) return;
  out = file_open(lemp,".c","w");
  if( out==0 ){
    fclose(in);
    return;
  }
  lineno = 1;
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate the include code, if any */
  tplt_print(out,lemp,lemp->include,lemp->includeln,&lineno);
  if( mhflag ){
    char *name = file_makename(lemp, ".h");
    fprintf(out,"#include \"%s\"\n", name); lineno++;
    free(name);
  }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate #defines for all tokens */
  if( mhflag ){
    char *prefix;
    fprintf(out,"#if INTERFACE\n"); lineno++;
    if( lemp->tokenprefix ) prefix = lemp->tokenprefix;
    else                    prefix = "";
    for(i=1; i<lemp->nterminal; i++){
      fprintf(out,"#define %s%-30s %2d\n",prefix,lemp->symbols[i]->name,i);
      lineno++;
    }
    fprintf(out,"#endif\n"); lineno++;
  }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate the defines */
  fprintf(out,"/* \001 */\n");
  fprintf(out,"#define YYCODETYPE %s\n",
    lemp->nsymbol>250?"int":"unsigned char");  lineno++;
  fprintf(out,"#define YYNOCODE %d\n",lemp->nsymbol+1);  lineno++;
  fprintf(out,"#define YYACTIONTYPE %s\n",
    lemp->nstate+lemp->nrule>250?"int":"unsigned char");  lineno++;
  print_stack_union(out,lemp,&lineno,mhflag);
  if( lemp->stacksize ){
    if( atoi(lemp->stacksize)<=0 ){
      ErrorMsg(lemp->filename,0,
"Illegal stack size: [%s].  The stack size should be an integer constant.",
        lemp->stacksize);
      lemp->errorcnt++;
      lemp->stacksize = "100";
    }
    fprintf(out,"#define YYSTACKDEPTH %s\n",lemp->stacksize);  lineno++;
  }else{
    fprintf(out,"#define YYSTACKDEPTH 100\n");  lineno++;
  }
  if( mhflag ){
    fprintf(out,"#if INTERFACE\n"); lineno++;
  }
  name = lemp->name ? lemp->name : "Parse";
  if( lemp->arg && lemp->arg[0] ){
    int i;
    i = strlen(lemp->arg);
    while( i>=1 && isspace(lemp->arg[i-1]) ) i--;
    while( i>=1 && (isalnum(lemp->arg[i-1]) || lemp->arg[i-1]=='_') ) i--;
    fprintf(out,"#define %sARGDECL ,%s\n",name,&lemp->arg[i]);  lineno++;
    fprintf(out,"#define %sXARGDECL %s;\n",name,lemp->arg);  lineno++;
    fprintf(out,"#define %sANSIARGDECL ,%s\n",name,lemp->arg);  lineno++;
  }else{
    fprintf(out,"#define %sARGDECL\n",name);  lineno++;
    fprintf(out,"#define %sXARGDECL\n",name);  lineno++;
    fprintf(out,"#define %sANSIARGDECL\n",name);  lineno++;
  }
  if( mhflag ){
    fprintf(out,"#endif\n"); lineno++;
  }
  fprintf(out,"#define YYNSTATE %d\n",lemp->nstate);  lineno++;
  fprintf(out,"#define YYNRULE %d\n",lemp->nrule);  lineno++;
  fprintf(out,"#define YYERRORSYMBOL %d\n",lemp->errsym->index);  lineno++;
  fprintf(out,"#define YYERRSYMDT yy%d\n",lemp->errsym->dtnum);  lineno++;
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate the action table.
  **
  ** Each entry in the action table is an element of the following 
  ** structure:
  **   struct yyActionEntry {
  **       YYCODETYPE            lookahead;
  **       YYACTIONTYPE          action;
  **       struct yyActionEntry *next;
  **   }
  **
  ** The entries are grouped into hash tables, one hash table for each
  ** parser state.  The hash table has a size which is the smallest
  ** power of two needed to hold all entries.
  */
  tablecnt = 0;

  /* Loop over parser states */
  for(i=0; i<lemp->nstate; i++){
    int tablesize;              /* size of the hash table */
    int j,k;                    /* Loop counter */
    int collide[2048];          /* The collision chain for the table */
    struct action *table[2048]; /* Build the hash table here */

    /* Find the number of actions and initialize the hash table */
    stp = lemp->sorted[i];
    stp->tabstart = tablecnt;
    stp->naction = 0;
    for(ap=stp->ap; ap; ap=ap->next){
      if( ap->sp->index!=lemp->nsymbol && compute_action(lemp,ap)>=0 ){
        stp->naction++;
      }
    }
    tablesize = 1;
    while( tablesize<stp->naction ) tablesize += tablesize;
    assert( tablesize<= sizeof(table)/sizeof(table[0]) );
    for(j=0; j<tablesize; j++){
      table[j] = 0;
      collide[j] = -1;
    }

    /* Hash the actions into the hash table */
    stp->tabdfltact = lemp->nstate + lemp->nrule;
    for(ap=stp->ap; ap; ap=ap->next){
      int action = compute_action(lemp,ap);
      int h;
      if( ap->sp->index==lemp->nsymbol ){
        stp->tabdfltact = action;
      }else if( action>=0 ){
        h = ap->sp->index & (tablesize-1);
        ap->collide = table[h];
        table[h] = ap;
      }
    }

    /* Resolve collisions */
    for(j=k=0; j<tablesize; j++){
      if( table[j] && table[j]->collide ){
        while( table[k] ) k++;
        table[k] = table[j]->collide;
        collide[j] = k;
        table[j]->collide = 0;
        if( k<j ) j = k-1;
      }
    }

    /* Print the hash table */
    fprintf(out,"/* State %d */\n",stp->index); lineno++;
    for(j=0; j<tablesize; j++){
      if( table[j]==0 ){
        fprintf(out,
          "  {YYNOCODE,0,0}, /* Unused */\n");
      }else{
        fprintf(out,"  {%4d,%4d, ",
          table[j]->sp->index,
          compute_action(lemp,table[j]));
        if( collide[j]>=0 ){
          fprintf(out,"&yyActionTable[%4d] }, /* ",
            collide[j] + tablecnt);
        }else{
          fprintf(out,"0                    }, /* ");
        }
        PrintAction(table[j],out,22);
        fprintf(out," */\n"); 
      }
      lineno++;
    }

    /* Update the table count */
    tablecnt += tablesize;
  }
  tplt_xfer(lemp->name,in,out,&lineno);
  lemp->tablesize = tablecnt;

  /* Generate the state table
  **
  ** Each entry is an element of the following structure:
  **    struct yyStateEntry {
  **      struct yyActionEntry *hashtbl;
  **      int mask;
  **      YYACTIONTYPE actionDefault;
  **    }
  */
  for(i=0; i<lemp->nstate; i++){
    int tablesize;
    stp = lemp->sorted[i];
    tablesize = 1;
    while( tablesize<stp->naction ) tablesize += tablesize;
    fprintf(out,"  { &yyActionTable[%d], %d, %d},\n",
      stp->tabstart,
      tablesize - 1,
      stp->tabdfltact); lineno++;
  }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate a table containing the symbolic name of every symbol */
  for(i=0; i<lemp->nsymbol; i++){
    sprintf(line,"\"%s\",",lemp->symbols[i]->name);
    fprintf(out,"  %-15s",line);
    if( (i&3)==3 ){ fprintf(out,"\n"); lineno++; }
  }
  if( (i&3)!=0 ){ fprintf(out,"\n"); lineno++; }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate code which executes every time a symbol is popped from
  ** the stack while processing errors or while destroying the parser. 
  ** (In other words, generate the %destructor actions) */
  if( lemp->tokendest ){
    for(i=0; i<lemp->nsymbol; i++){
      struct symbol *sp = lemp->symbols[i];
      if( sp==0 || sp->type!=TERMINAL ) continue;
      fprintf(out,"    case %d:\n",sp->index); lineno++;
    }
    for(i=0; i<lemp->nsymbol && lemp->symbols[i]->type!=TERMINAL; i++);
    if( i<lemp->nsymbol ){
      emit_destructor_code(out,lemp->symbols[i],lemp,&lineno);
      fprintf(out,"      break;\n"); lineno++;
    }
  }
  for(i=0; i<lemp->nsymbol; i++){
    struct symbol *sp = lemp->symbols[i];
    if( sp==0 || sp->type==TERMINAL || sp->destructor==0 ) continue;
    fprintf(out,"    case %d:\n",sp->index); lineno++;
    emit_destructor_code(out,lemp->symbols[i],lemp,&lineno);
    fprintf(out,"      break;\n"); lineno++;
  }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate code which executes whenever the parser stack overflows */
  tplt_print(out,lemp,lemp->overflow,lemp->overflowln,&lineno);
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate the table of rule information 
  **
  ** Note: This code depends on the fact that rules are number
  ** sequentually beginning with 0.
  */
  for(rp=lemp->rule; rp; rp=rp->next){
    fprintf(out,"  { %d, %d },\n",rp->lhs->index,rp->nrhs); lineno++;
  }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate code which execution during each REDUCE action */
  for(rp=lemp->rule; rp; rp=rp->next){
    fprintf(out,"      case %d:\n",rp->index); lineno++;
    fprintf(out,"        YYTRACE(\"%s ::=",rp->lhs->name);
    for(i=0; i<rp->nrhs; i++) fprintf(out," %s",rp->rhs[i]->name);
    fprintf(out,"\")\n"); lineno++;
    emit_code(out,rp,lemp,&lineno);
    fprintf(out,"        break;\n"); lineno++;
  }
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate code which executes if a parse fails */
  tplt_print(out,lemp,lemp->failure,lemp->failureln,&lineno);
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate code which executes when a syntax error occurs */
  tplt_print(out,lemp,lemp->error,lemp->errorln,&lineno);
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Generate code which executes when the parser accepts its input */
  tplt_print(out,lemp,lemp->accept,lemp->acceptln,&lineno);
  tplt_xfer(lemp->name,in,out,&lineno);

  /* Append any addition code the user desires */
  tplt_print(out,lemp,lemp->extracode,lemp->extracodeln,&lineno);

  fclose(in);
  fclose(out);
  return;
}

/* Generate a header file for the parser */
void ReportHeader(lemp)
struct lemon *lemp;
{
  FILE *out, *in;
  char *prefix;
  char line[LINESIZE];
  char pattern[LINESIZE];
  int i;

  if( lemp->tokenprefix ) prefix = lemp->tokenprefix;
  else                    prefix = "";
  in = file_open(lemp,".h","r");
  if( in ){
    for(i=1; i<lemp->nterminal && fgets(line,LINESIZE,in); i++){
      sprintf(pattern,"#define %s%-30s %2d\n",prefix,lemp->symbols[i]->name,i);
      if( strcmp(line,pattern) ) break;
    }
    fclose(in);
    if( i==lemp->nterminal ){
      /* No change in the file.  Don't rewrite it. */
      return;
    }
  }
  out = file_open(lemp,".h","w");
  if( out ){
    for(i=1; i<lemp->nterminal; i++){
      fprintf(out,"#define %s%-30s %2d\n",prefix,lemp->symbols[i]->name,i);
    }
    fclose(out);  
  }
  return;
}

/* Reduce the size of the action tables, if possible, by making use
** of defaults.
**
** In this version, if all REDUCE actions use the same rule, make
** them the default.  Only default them if there are more than one.
*/
void CompressTables(lemp)
struct lemon *lemp;
{
  struct state *stp;
  struct action *ap;
  struct rule *rp;
  int i;
  int cnt;

  for(i=0; i<lemp->nstate; i++){
    stp = lemp->sorted[i];

    /* Find the first REDUCE action */
    for(ap=stp->ap; ap && ap->type!=REDUCE; ap=ap->next);
    if( ap==0 ) continue;

    /* Remember the rule used */
    rp = ap->x.rp;

    /* See if all other REDUCE acitons use the same rule */
    cnt = 1;
    for(ap=ap->next; ap; ap=ap->next){
      if( ap->type==REDUCE ){
        if( ap->x.rp!=rp ) break;
        cnt++;
      }
    }
    if( ap || cnt==1 ) continue;

    /* Combine all REDUCE actions into a single default */
    for(ap=stp->ap; ap && ap->type!=REDUCE; ap=ap->next);
    assert( ap );
    ap->sp = Symbol_new("{default}");
    for(ap=ap->next; ap; ap=ap->next){
      if( ap->type==REDUCE ) ap->type = NOT_USED;
    }
    stp->ap = Action_sort(stp->ap);
  }
}
