# Development process 
- This app was developed with Test-Driven-Development process. Tests were written before production code is implemented.  By this way, application logic can be tested throughly step by step.
    
## Clean architecture with 3 layers  
- Data (for API)  
![data_layer](./images/data_layer.png)
- Domain (for business logic and models)
![domain_layer](./images/domain_layer.png)
- Presentation (for UI logic, with MVVM)  
![ui_layer](./images/ui_layer.png)

## Libraries
- API (with [Retrofit](https://github.com/square/retrofit))  
- Reactive programming with [Rxjava](https://github.com/ReactiveX/RxJava)  
- JSON Parsing [Moshi](https://github.com/square/moshi)  

- Mocking [mockk](https://github.com/mockk/mockk)  
- UI Testing (with [Kakao](https://github.com/KakaoCup/Kakao))
  
# Getting started  
  
### Android Studio  
1. Download this repository extract and open the folder  
2. Create a file named  `apikey.properties` and your api to `WEATHER_FORECASE_API_KEY`  
3. Build and start the app
   
  