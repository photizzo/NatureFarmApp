# NatureFarmApp
A app for farmers to use to capture their data and farm map

### Introduction
A app for farmers to use to capture their data and farm map

### Architecture
Clean Architecture with MVVM

### Used libraries
**Dagger2** - Dagger2 was used for dependency injection.</br>
**RxJava2** - RxJava2 was used for threading and data stream management.</br>
**AndroidKtx** - For cool extensions to Android classes.</br>
**Architecture Components** - For Lifecycle in the presentation layer.</br>
**JUnit** - For Unit test assertions etc.</br>


### Process

I decided to use clean architecture and also break down the project into modules, I thought of exploring `Kotlin Flows` extensively but decided otherwise because `RxJava2` is (as at the time of writing more mature for my chosen presentation architecture MVVM), I also decided to use different models for each layer of the architecture so I could do some data cleaning and conversion to fit the models into each layer. I wrote interfaces to represent repositories in the domain layer and then wrote `UseCase`s different actions

Afterwards I started writing the `data` layer, here I began by writing models for each of the apps entities/data objects and then interfaces that described classes that would interact the cache layer

Finally I started work on the UI layer. I used the MVVM architecture supported by `ViewModel` and `RxJava2`.


### Possible Improvements

I had a lot of fun building this. There are some improvements I intend to make.

- Write tests for UI. </br>
- Use MockWebServer to test `Remote` layer API calls.</br>
- Improve UI and add a animations</br>
- Write Unit tests.


### Build Instructions
- Clone repository.</br>
- Run with Android Studio 3.4 and above</br>
