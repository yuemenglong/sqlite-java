/********* From the file "assert.h" ************************************/
void myassert();
#ifndef NDEBUG
#  define assert(X) if(!(X))myassert(__FILE__,__LINE__)
#else
#  define assert(X)
#endif

