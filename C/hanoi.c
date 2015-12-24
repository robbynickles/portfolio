//Tower of Hanoi puzzle solver
#include <stdio.h>
solveHanoi(int discs, int src, int dest, int help) {
  if (discs == 1) printf("Move disc 1 from peg %d to peg %d.\n",src, dest);
  else {
    solveHanoi(discs-1,src,help,dest);
    printf("Move disc %d from peg %d to peg %d.\n",discs,src,dest);
    solveHanoi(discs-1,help,dest,src);
  }
}
int main(int argc, char *argv[]) {
  if (argc > 1) { 
    int discs = *argv[1] - '0';
    printf("\n******************************************** \n");
    printf("\n\tHere are the minumum moves to solve the %d-disc Tower of Hanoi puzzle.\nPeg one is the source, peg two is the helper, and peg three is the destination.\n", discs);
    printf("\n********************************************* \n");
    
    solveHanoi(discs,1,3,2);
    
    printf("*********************************************\n");
    printf("NUMBER OF STEPS: %d\n",(1 << discs) - 1);
  } else 
    puts("Try again. Enter a number of discs.");
  return 0;
}

