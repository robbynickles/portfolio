//erat.c implements the sieve of eratosthenes
#include <stdio.h>
erat(int *prime, int n) {
  if (prime != NULL) {
    int i, divisor;
    for (i = 2; i <= n; i++) prime[i] = 1;
    for (divisor = 2; divisor * divisor <= n; divisor++) 
      if (prime[divisor]) 
	for (i = 2 * divisor; i <= n; i += divisor) 
	  prime[i] = 0;
  } else puts("array passed to erat was null");
}

/*

#include <stdio.h>

main() {
  int n = 100, prime[n+1], i; //It's best to create the array and then pass a pointer to a function (in C) that then modifies the existing array. 
  erat(prime, n);
  for (i = 2; i < n; i++)
    if (prime[i]) printf("%d\n\n", i);
}
*/
