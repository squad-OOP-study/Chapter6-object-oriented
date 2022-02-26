# Chapter6-object-oriented

---

## 📌 어플리케이션 영역과 메인 영역
##
로버트 C 마틴(Robert C. Martin)은 소프트웨어를 두 개의 영역으로 구분해서 설명
- 고수준 정책 및 저수준 구현을 포함한 **애플리케이션 영역**
- 어플리케이션이 동작하도록 각 객체들을 연결해 주는 **메인 영역**
##
SOLID 설계 원칙으로 프로그램을 개발하다보면 클라이언트는 고수준 모듈을 의존하지만 실제로 사용할 때 콘크리트 클래스의 객체가 필요하다.
```java
class Worker {

    public void run() {
        JobQueue jobQueue = ...;
        Transcoder transcoder = ...;
        ...
    }
}
```
###
실제 객체를 구할 방법으로 Locator 라는 객체를 사용하기로 결정했다.
```java
class Worker {

    public void run() {
        JobQueue jobQueue = ...;
        Transcoder transcoder = ...;
        ...
    }
}
```
##
이때 누가 Locator 를 초기화 해주고 Worker 를 정할까라는 의문이 생기는데  
여기서 메인(main) 영역이 등장한다. 메인 영역은 다음과 같은 일을 한다.
- 애플리케이션 영역에서 사용될 객체를 생성
- 각 객체 간의 의존 관계를 설정
- 애플리케이션을 실행

```java
public class Main {

    public static void main(String[] args) {
        // 상위 수준 모듈에서 사용될 하위 수준 모듈 객체 생성
        JobQueue jobQueue = new FileJobQueue();
        
        // 상위 수준 모듈이 하위 수준 모듈을 사용할 수 있도록 Locator 초기화 
        Locator locator = new Locator(jobQueue);
        Locator.init(locator);
        ...
    }
}
```
해당 예시는 자바 코드가 더 와닿는 것 같다.
##
메인 영역의 역할을 보면 알 수 있듯이 모든 의존은 메인 영역에서 어플리케이션 영역으로 향한다. 반대의 경우 어플리케이션 영역에서 메인 영역으로이 의존은 존재하지 않는다.

따라서 어플리케이션에서 사용될 객체를 교체하기 위해 메인 영역의 코드를 수정해도 영향을 끼치진 않는다.

그리고 어플리케이션에서 사용될 객체를 제공하는 책임을 갖는 객체를 서비스 로케이터(Service Locator)라고 부른다.
<img width="625" height="600" alt="image" src="https://user-images.githubusercontent.com/79504043/155853248-6c171420-8ac1-4b5c-862c-4a4761ea0e9e.png">

###
### 🚨 서비스 로케이터의 단점
서비스 로케이터의 가장 단점은 동일 타입의 객체가 다수 필요할 경우, 각 객체 별로 제공 메서드를 만들어 주어야 한다는 점이다.
```java
public class ServiceLocator {
	public JobQueue getJobQueue1() { ... }
	public JobQueue getJobQueue2() { ... }
	...
}
```

### + 추가 
**Mark Seemann - Service Locator is an Anti-Pattern 번역**  
####
서비스 로케이터는 안티패턴으로 피해야 하는 방식이다.  
왜 안티 패턴인지 더 살펴보자. 서비스 로케이터를 사용했을 때 나타나는 문제는 클래스의 의존성을 숨겨서 컴파일 중 오류가 나타나지 않고 대신 런타임에서 오류를 찾을 수 있다. 
이전에 작성한 코드와 호환이 되지 않도록 코드를 변경한 경우에는 어떤 부분에 의존성이 있는지 명확하지 않아 
새로운 코드를 작성할 때마다 코드를 유지보수하는 일이 더욱 어려워진다.  

결론적으로 서비스 로케이터를 사용하면 지금 변경한 코드가 문제를 만드는 변경인지 아닌지 판단하기 더욱 어렵다. 코드를 수정하거나 작성하기 위해서는 서비스 로케이터를 사용하는 어플리케이션 **'전체'** 를 모두 이해해야 한다.

---

## 📌 DI(Dependency Injection) 을 이용한 의존 객체 사용
##
콘크리트 클래스를 직접 사용해서 객체를 생성하게 되면 의존 역전 원칙을 위반하게 되며, 결과적으로 확장 폐쇄 원칙을 위반하게 된다. 이런 단점을 보완하기 위한 방법이 DI이다. DI는 필요한 객체를 직접 생성하거나 찾지 않고 외부에서 넣어 주는 방식이다.

##
## 생성자 방식과 설정 메서드 방식
##

### 생성자 방식

```java
public class JobCLI {
	private JobQueue jobQueue;
	public  JobCLI(JobQueue jobQueue) {
		this.jobQueue = jobQueue;
	}
	public void interact() {
		...
	}
}
```
### 설정 메서드 방식
```java
public class Worker {
	private JobQueue jobQueue;
	private Transcoder transcoder;

	public void setJobQueue(JobQueue jobQueue) {
		this.jobQueue = jobQueue;
	}

	public void setTranscoder(Transcoder transcoder) { 
		this.transcoder = transdocer;
	}
	...
}
```

### 각 방식의 장단점
**생성자 방식**의 장점은 객체를 생성하는 시점에 필요한 모든 의존 객체를 준비할 수 있다는 것이다.
그 이유는 생성자 방식은 객체를 생성하는 시점에 필요한 모든 의존 객체를 준비할 수 있기 때문이다.
생성자 방식은 생성자를 통해서 필요한 의존 객체를 전달받기 때문에, 객체를 생성하는 시점에서 의존객체가 정상인지 확인할 수 있다.
생성 시점에 의존 객체를 모두 받기 때문에, 한 번 객체가 생성되면 객체가 정상적으로 동작함을 보장할 수 있게 된다.  

**설정 메서드 방식**의 장점은 은 객체를 생성한 이후에 의존 객체를 설정할 수 있기 때문에, 어떤 이유로 인해 의존할 객체가 나중에 생성된다면 설정 메서드 방식을 사용해야 한다.
만약 의존 객체를 설정하지 못한 상태에서 객체의 메서드를 싱행한다면 NPE 가 발생할 수도 있다.

---
