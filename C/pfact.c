#include <stdio.h>
#include <stdlib.h>

typedef struct node {
  int x;
  struct node *next;
} node;

node *newNode(int x) {
  node *node = malloc(sizeof(node));
  node->x = x;
  node->next = NULL;
  return node;
}

node *extend(node *n1, node *n2) {
  n1->next = n2;
  return n1;
}

// ---> a list of the prime factors of n , such that the product of all items == n. 
node *pfact( int x, int prime[]) {

  // If x is prime, it's prime factors are x and 1.
  if ( prime[ x ] ) return newNode( x );

  // Scan for the lowest prime factor.
  int i;
  for ( i = 2; i < x; i++ ) 
    if ( prime[ i ] && ( x % i == 0 ) ) 

      // The prime factors of x are
      // i and the prime factors of x / i
      return extend( newNode( i ), pfact( x/i, prime ) );
}

// Useful function declaration. Reminder that to use this function, supply the .o file with this function to the compiler.
void erat(int *, int);


main(int argc, char **argv) {
  if (argc > 1) {
    int N = atoi(argv[1]), primes[N + 1];
    erat(primes, N); 
    node *primeFactors = pfact(N, primes), *current = primeFactors;
    do printf("%d ", current->x); while (current = current->next);
    printf("\n");
  } else puts("Try again. Enter a number.");
}
