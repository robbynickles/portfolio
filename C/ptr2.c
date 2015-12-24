/*

  If I have pointers *a and *b and then set b = a, and then change a, does the thing 
  b points at change too? Yes. 

  If I set a pointer to a value initialized inside a procedure, does the value
  persist?
  No. Use static.

*/
#include <stdio.h>
#include <stdlib.h>

setToSomeValue(void *ptr) {
  int x = 3;
  ptr = &x;
}
int *setToSomeValue2(int *ptr) {
  int x = 3;
  ptr = malloc(sizeof(int));
  ptr = &x;
  return ptr;
}

test(int *a, int *b) {
  b = a;
  *a = 76;
  printf("\nFrom test()\n");
  printf("*a = %d and *b = %d\n", *a, *b);
}
main() {
  int x = 3, y = 4, *a = &x, *b = a;
  a = &y;
  printf("*b = %d\n",*b);
  printf("*a = %d\n",*a);

  /* int *ptr;
  setToSomeValue2(ptr);
  printf("ptr = %d",(int) *ptr);
  */
  int u = 3, v = 4, *m = &u, *n = &v;
  test(m, n);
}
