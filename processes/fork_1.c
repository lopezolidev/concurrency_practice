#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>

int main(int argc, char* argv[]){
    int id = fork();
    
    if (id != 0) {
        printf("Hello world, from main process \n");
        fork();
    } 
        printf("Hello world, from any other process \n") ;

    // total number of processes created: 3
    return 0;
}