# Spyzie
Track activities remotely on any Android devices (phones, tablets).
<br/>

<h2>Description: </h2>This service operates seamlessly in the background, without disrupting the user experience. It periodically gathers essential device information—including location, messages, contacts, and call logs—and transmits the data to a secure backend server. Even in the absence of an internet connection, all collected data is reliably stored in the device's local database and synchronized automatically once connectivity is restored.

<h2>Library reference resources: </h2>
1. RxJava2/RxAndroid: https://www.vogella.com/tutorials/RxJava/article.html
<br/>
2. Dagger2: http://frogermcs.github.io/dependency-injection-with-dagger-2-the-api/
<br />
http://frogermcs.github.io/dependency-injection-with-dagger-2-custom-scopes/
<br />
3. FastAndroidNetworking: https://github.com/amitshekhariitbhu/Fast-Android-Networking
<br />
4. ObjectBox: https://github.com/objectbox/objectbox-examples/
<br />
5. WorkManager: https://medium.com/androiddevelopers/introducing-workmanager-2083bcfc4712
<br />
6. Android getting GPS Location with Fused Location Provider API
<br />
<h2>Project architecture</h2> The project is architected using the Model-View-Presenter (MVP) design pattern, ensuring a clear separation of concerns. This structure enhances testability, maintainability, and scalability by decoupling the presentation layer from business logic and data handling components.

### License
```
   Copyright (C) 2020 tantd90.ktmt@gmail.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
