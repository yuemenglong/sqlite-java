/********************** From the file "assert.c" ****************************/
/*
** A more efficient way of handling assertions.
*/
void myassert(file,line)
char *file;
int line;
{
  fprintf(stderr,"Assertion failed on line %d of file \"%s\"\n",line,file);
  exit(1);
}
