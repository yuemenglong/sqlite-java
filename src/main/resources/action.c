/****************** From the file "action.c" *******************************/
/*
** Routines processing parser actions in the LEMON parser generator.
*/

/* Allocate a new parser action */
struct action *Action_new(){
  static struct action *freelist = 0;
  struct action *new;

  if( freelist==0 ){
    int i;
    int amt = 100;
    freelist = (struct action *)malloc( sizeof(struct action)*amt );
    if( freelist==0 ){
      fprintf(stderr,"Unable to allocate memory for a new parser action.");
      exit(1);
    }
    for(i=0; i<amt-1; i++) freelist[i].next = &freelist[i+1];
    freelist[amt-1].next = 0;
  }
  new = freelist;
  freelist = freelist->next;
  return new;
}

/* Compare two actions */
static int actioncmp(ap1,ap2)
struct action *ap1;
struct action *ap2;
{
  int rc;
  rc = ap1->sp->index - ap2->sp->index;
  if( rc==0 ) rc = (int)ap1->type - (int)ap2->type;
  if( rc==0 ){
    assert( ap1->type==REDUCE && ap2->type==REDUCE );
    rc = ap1->x.rp->index - ap2->x.rp->index;
  }
  return rc;
}

/* Sort parser actions */
struct action *Action_sort(ap)
struct action *ap;
{
  ap = (struct action *)msort(ap,&ap->next,actioncmp);
  return ap;
}

void Action_add(app,type,sp,arg)
struct action **app;
enum e_action type;
struct symbol *sp;
char *arg;
{
  struct action *new;
  new = Action_new();
  new->next = *app;
  *app = new;
  new->type = type;
  new->sp = sp;
  if( type==SHIFT ){
    new->x.stp = (struct state *)arg;
  }else{
    new->x.rp = (struct rule *)arg;
  }
}
