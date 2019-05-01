/*Breeana George
CSC 139 Section 3
Testing in Linux*/
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/timeb.h>
#include <semaphore.h>

#define MAX_SIZE 100000000
#define MAX_THREADS 16
#define RANDOM_SEED 6923
#define MAX_RANDOM_NUMBER 6000

// Global variables
//For timing
long gRefTime; 
//The array that will hold the data
int gData[MAX_SIZE]; 

//Number of threads
int gThreadCount; 
//Number of threads that are done at a certain point. Whenever a thread is done, it increments this. Used with the semaphore-based solution
int gDoneThreadCount;
//The minimum value found by each thread 
int gThreadMin[MAX_THREADS];
//Is this thread done? Used when the parent is continually checking on child threads
bool gThreadDone[MAX_THREADS]; 

// Semaphores
//To notify parent that all threads have completed or one of them found a zero
sem_t completed; 
//Binary semaphore to protect the shared variable gDoneThreadCount
sem_t mutex; 

 //Sequential FindMin (no threads)
int SqFindMin(int size);
//Thread FindMin but without semaphores 
void *ThFindMin(void *param); 
//Thread FindMin with semaphores 
void *ThFindMinWithSemaphore(void *param); 
// Search all thread minima to find the minimum value found in all threads 
int SearchThreadMin(); 
 //Generate the input array 
void GenerateInput(int size, int indexForZero);
//Calculate the indices to divide the array into T divisions, one division per thread
void CalculateIndices(int arraySize, int thrdCnt, int indices[MAX_THREADS][3]); 
//Get a random number between min and max
int GetRand(int min, int max);

//Timing functions
long GetMilliSecondTime(struct timeb timeBuf);
long GetCurrentTime(void);
void SetTime(void);
long GetTime(void);

int main(int argc, char *argv[]){

	pthread_t tid[MAX_THREADS];  
	pthread_attr_t attr[MAX_THREADS];
	int indices[MAX_THREADS][3];
	int i, indexForZero, arraySize, min;

	// Code for parsing and checking command-line arguments
	if(argc != 4){
		fprintf(stderr, "Invalid number of arguments!\n");
		exit(-1);
	}
	if((arraySize = atoi(argv[1])) <= 0 || arraySize > MAX_SIZE){
		fprintf(stderr, "Invalid Array Size\n");
		exit(-1);				
	}
	gThreadCount = atoi(argv[2]);				
	if(gThreadCount > MAX_THREADS || gThreadCount <=0){
		fprintf(stderr, "Invalid Thread Count\n");
		exit(-1);				
	}
	indexForZero = atoi(argv[3]);
	if(indexForZero < -1 || indexForZero >= arraySize){
		fprintf(stderr, "Invalid index for zero!\n");
		exit(-1);
	}

    GenerateInput(arraySize, indexForZero);

    CalculateIndices(arraySize, gThreadCount, indices); 
	
	// Code for the sequential part
	SetTime();
	min = SqFindMin(arraySize);
	printf("Sequential search completed in %ld ms. Min = %d\n", GetTime(), min);

	
	// Threaded with parent waiting for all child threads
	SetTime();

	// Write your code here
	/* Initialize threads, create threads, and then let the parent wait for all threads using pthread_join. The thread start function is ThFindMin. Don't forget to properly initialize shared variables*/
	for(i=0; i < gThreadCount; i++){
     	pthread_attr_init(&attr[i]);
		pthread_create(&(tid[i]), &(attr[i]), ThFindMin, indices[i]);	 
	}
	for(i=0; i < gThreadCount; i++){
	pthread_join(tid[i], NULL);
	}
	
    min = SearchThreadMin();
	printf("Threaded FindMin with parent waiting for all children completed in %ld ms. Min = %d\n", GetTime(), min);
	
	// Multi-threaded with busy waiting (parent continually checking on child threads without using semaphores)
	SetTime();

	// Write your code here
    /* Don't use any semaphores in this part. Initialize threads, create threads, and then make the parent continually check on all child threads. The thread start function is ThFindMin. Don't forget to properly initialize shared variables */
	for(i=0; i < gThreadCount; i++){
		gThreadDone[i] = false;
		pthread_attr_init(&attr[i]);
		pthread_create(&tid[i], &attr[i], ThFindMin, indices[i]);
	}
	volatile int k = 0;
	volatile int threadCount = 0;
	while(gThreadMin[k] != 0 && threadCount <= gThreadCount){
		if(gThreadDone[k] == true){
			threadCount++;
		}
		k += k % gThreadCount;
	}
	
	for(i=0; i < gThreadCount; i++){
		pthread_cancel(tid[i]);
	}
	
    min = SearchThreadMin();
	printf("Threaded FindMin with parent continually checking on children completed in %ld ms. Min = %d\n", GetTime(), min);
	

	// Multi-threaded with semaphores  
	SetTime();

    // Write your code here
	/* Initialize threads, create threads, and then make the parent wait on the "completed" semaphore. The thread start function is ThFindMinWithSemaphor. Don't forget to properly initialize shared variables and semaphores using sem_init 
	// Semaphores
	//To notify parent that all threads have completed or one of them found a zero
	sem_t completed; 
	//Binary semaphore to protect the shared variable gDoneThreadCount
	sem_t mutex; 

	*/ 
	sem_init(&completed, 0, 1);
	sem_init(&mutex, 0, 1);
	for(i=0; i < gThreadCount; i++){
		pthread_attr_init(&attr[i]);
		pthread_create(&tid[i], &attr[i], ThFindMinWithSemaphore, indices[i]);	
	}
	sem_wait(&completed);
	
	for(i=0; i < gThreadCount; i++){
		pthread_cancel(tid[i]);
	}

	min = SearchThreadMin();
	printf("Threaded FindMin with parent waiting on a semaphore completed in %ld ms. Min = %d\n", GetTime(), min);
}

// Write a regular sequential function to search for the minimum value in the array gData
int SqFindMin(int size) {
	int min = gData[0];
	//printf("min in sqffindmin1 %d\n", min);
	for(int i = 0; i < size; i++){
		if(gData[i] < min){
			min = gData[i];
			if(gData[i]==0){
				return min;
			}
		}	
	}
	return min;
}

/* Write a thread function that searches for the minimum value in one division of the array. When it is done, this function should put the minimum in gThreadMin[threadNum] and set gThreadDone[threadNum] to true */   
void* ThFindMin(void *param){
	int threadNum = ((int*)param)[0];
	int threadStart = ((int*)param)[1];
	int threadEnd = ((int*)param)[2];
	int min = gData[threadStart];
    for(int i = threadStart; i <= threadEnd; i++){   
        if(gData[i] < min){   
			min = gData[i];
			if(gData[i] == 0){
				min = 0;
				break;
			}	
        }
    }
	gThreadDone[threadNum] = true;
	gThreadMin[threadNum] = min;
}

/* Write a thread function that searches for the minimum value in one division of the array. When it is done, this function should put the minimum in gThreadMin[threadNum]. If the minimum value in this division is zero, this function should post the "completed" semaphore. If the minimum value in this division is not zero, this function should increment gDoneThreadCount and post the "completed" semaphore if it is the last thread to be done. Don't forget to protect access to gDoneThreadCount with the "mutex" semaphore*/     
void* ThFindMinWithSemaphore(void *param) {
	int threadNum = ((int*)param)[0];
	int threadStart = ((int*)param)[1];
	int threadEnd = ((int*)param)[2];
	int min = gData[threadStart];
    for(int i = threadStart; i <= threadEnd; i++){   
        if(gData[i] < min){   
			min = gData[i];
			if(gData[i] == 0){
				min = 0;
				break;
			}
        }
    }
	sem_wait(&mutex);
	gDoneThreadCount++;
	sem_post(&mutex);
	gThreadMin[threadNum] = min;
	if(gDoneThreadCount>=gThreadCount || min == 0){
		sem_post(&completed);
	}
}

int SearchThreadMin() {
    int i, min = MAX_RANDOM_NUMBER + 1;
    for(i =0; i<gThreadCount; i++) {
        if(gThreadMin[i] == 0)
            return 0;
		if(gThreadDone[i] == true && gThreadMin[i] < min)
			min = gThreadMin[i];
	}
	return min;
}

/* Write a function that fills the gData array with random numbers between 1 and MAX_RANDOM_NUMBER. If indexForZero is valid and non-negative, set the value at that index to zero */
void GenerateInput(int size, int indexForZero) {
	for(int i=0; i< size; i++){
		if(i == indexForZero){
			gData[i] = 0;
		}else{	
			int rand = GetRand(1, MAX_RANDOM_NUMBER);
			gData[i] = rand;
		}	
	}	
}

/* Write a function that calculates the right indices to divide the array into thrdCnt equal divisions. For each division i, indices[i][0] should be set to the division number i, indices[i][1] should be set to the start index, and indices[i][2] should be set to the end index */
void CalculateIndices(int arraySize, int thrdCnt, int indices[MAX_THREADS][3]) {
	int chunkSize = arraySize / thrdCnt;
	for(int i = 0; i < thrdCnt; i++){
		int start = i * chunkSize;
		int end = start + chunkSize - 1;
		if(i == thrdCnt - 1){
			end = arraySize - 1;
		}
		indices[i][0]=i;
		indices[i][1]=start;
		indices[i][2]=end;
		//printf("i %d start %d end %d", indices[i][0], indices[i][1], indices[i][2] );
	}	
}

// Get a random number in the range [x, y]
int GetRand(int x, int y) {
    int r = rand();
    r = x + r % (y-x+1);
    return r;
}

long GetMilliSecondTime(struct timeb timeBuf){
	long mliScndTime;
	mliScndTime = timeBuf.time;
	mliScndTime *= 1000;
	mliScndTime += timeBuf.millitm;
	return mliScndTime;
}

long GetCurrentTime(void){
	long crntTime=0;
	struct timeb timeBuf;
	ftime(&timeBuf);
	crntTime = GetMilliSecondTime(timeBuf);
	return crntTime;
}

void SetTime(void){
	gRefTime = GetCurrentTime();
}

long GetTime(void){
	long crntTime = GetCurrentTime();
	return (crntTime - gRefTime);
}

