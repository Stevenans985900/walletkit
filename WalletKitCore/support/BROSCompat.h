//
//  BROSCompat.h
//
//  Created by Aaron Voisine on 2/14/20.
//  Copyright (c) 2015 breadwallet LLC
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//  THE SOFTWARE.

#ifndef BROSCompat_h
#define BROSCompat_h

#include <pthread.h>
#include <string.h>         // strlcpy()

//#if defined (__linux__)
//#include <bsd/stdlib.h>     // arc4random()
//#endif

#ifdef __cplusplus
extern "C" {
#endif

#define PTHREAD_NULL   ((pthread_t) NULL)

typedef void* (*ThreadRoutine) (void*);         // pthread_create

extern int
pthread_setname_brd(pthread_t,  const char *name);

extern void
pthread_yield_brd (void);

extern int
pthread_cond_timedwait_relative_brd (pthread_cond_t *cond,
                                     pthread_mutex_t *mutex,
                                     const struct timespec *reltime);

extern void
random_bytes_brd (void *bytes, size_t bytesCount);

#if defined (__linux__) && !defined(strlcpy)
extern size_t
strlcpy (char *dst, const char *src, size_t siz);
#endif

#ifdef __cplusplus
}
#endif

#endif // BROSCompat_h

