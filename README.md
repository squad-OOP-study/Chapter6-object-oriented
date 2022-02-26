# Chapter6

# DI (Dependency Injection)와 서비스 로케이터

- DI는 `의존성 주입`의 약자로 필요한 객체를 직접 생성하거나 찾지 않고 외부에서 넣어주는 방식을 말한다.
- DI와 서비스 로케이터는 객체를 연결하기 위해 사용되는 방법이다.

---

## 예제
- 외부에서 객체를 넣어준다는 것은 아래 예제와 같다.

```kotlin
interface JobQueue {
    fun process()
}

class DbJobQueue : JobQueue {
    override fun process() {}
}
class FileJobQueue : JobQueue {
    override fun process() {}
}

class Worker(jobQueue: JobQueue) {
    private val jobQueue = jobQueue

    fun process() {
        jobQueue.process()
    }
}

fun main() {
    val fileJoqQueue = FileJobQueue()
    val worker = Worker(fileJoqQueue)
    
    worker.process()
}
```

- `SomethingClass`에서 `worker`객체의 생성자에 `JobQueue`인터페이스를 상속받는 어떤 클래스를 넣어주게 되면 `FileJobQueue` 뿐만 아니라 `DbJobQueue`등 `JobQueue`를 상속받는 어떤 클래스든 받을 수 있게 된다.
- 이렇게 되면 DB, File이 아닌 다른 어떤 종류의 JobQueue클래스가 생성됐을 때 단순히 인자값만 변경해주게 되면 `JobQueue`에 대한 어떤 변경사항도 없이 사용할 클래스를 교체할 수 있게 된다.
- 이런것처럼 실제 구현을 담당하는 클래스만 변경해도 정상적으로 작동할 수 있도록 하는것이 DI(Dependency Injection, 의존성 주입)이라고 한다.
- 위에서는 main함수에서 객체를 조립하는 역할까지 가지고 있었는데, 이것을 Assembler 객체를 이용해 따로 조립 역할을 맡기게 할 수 있다. 
```kotlin
...
fun main() {
    val assembler = Assembler()
    val worker = assembler.worker
    worker.process()
}

class Assembler {
    val jobQueue = FileJobQueue()
    val worker = Worker(jobQueue)
}
```
- 이와 같이 어셈블러를 통해 조립을 하게 되면 XML 파일에서 객체 정보를 읽어오고 생성과 조립을 할 수도 있다. 이러한 방법을 채택했던 것이 스프링 프레임워크이다.

---

## 의존성 주입 방법
1. **생성자 방식**
    - 위에서 살펴본 방식이 생성자 방식으로 의존성을 주입한 방법으로 객체가 생성되는 시점에 모든 의존 관계가 정해지기 때문에 의존하려는 객체가 정상인지 알 수 있다는 장점이 있다. 단, 의존 객체를 먼저 생성해야 의존성 주입이 가능하다는 단점이 있다.

2. **메서드 방식**
    - `setXXX()`와 같은 메서드를 이용해 의존성을 주입하는 방법으로 런타임에 의존성을 변경할 수 있다는 장점이 있다. 단, 어떤 이유로 인해 NPE가 발생할 수 있어 문제가 될 가능성이 있다.

---

## DI와 테스트
- 단위 테스트는 한 클래스의 기능을 테스트하는데 초점을 맞추며 아직 구현이 완료되지 않은 클래스에 대해서는 `Mock`객체로 설정해 동작을 유추할 수 있다. 그러나 이러한 테스트는 테스트하려는 객체가 의존성 주입을 지원해야만 쉽게 테스트가 가능하다. 왜냐하면 미구현된 클래스를 이용해 구현된 클래스를 테스트하려면 미구현된 클래스가 구현된 것으로 인식해야되는데 이렇게 되면 구현된 클래스를 테스트에 맞춰 변경해야할 수 있기 때문이다.
- `Mock`객체를 생성하는 방법은 `Mokito`등의 라이브러리를 이용해 구현이 가능하다.

---

## 서비스 로케이터
- 안드로이드는 액티비티가 서로 각자의 진입점을 가지고 있기 때문에 DI를 처리하기에는 까다롭다. 왜냐하면 다른 프로그램들은 `main`이라는 시작점이 존재하는데 안드로이드는 컴포넌트들이 각자의 진입점을 가지고 있어 DI를 처리하기 위해서는 특정 라이브러리를 이용해야 한다.
- 이러한 안드로이드의 환경적인 제약 때문에 `서비스 로케이터`라는 방법이 알려지게 됐따.
- 객체 등록 방식의 서비스 로케이터를 구현할 수 있는데, 그 방법은 서비스 로케이터를 생성할 때 사용할 객체를 전달한 후 서비스 로케이터 인스턴스를 지정하고 참조하기 위한 static 메서드를 제공한다.

- **장점**
    - 생성자나 set 메서드를 이용해 서비스 로케이터가 제공할 객체를 등록하고 사용하는 코드에서는 get 메서드로 사용할 객체를 구하기만 하면 되기 때문이다. 

- **단점**
    - 서비스 로케이터에 객체를 등록하는 인터페이스가 노출되어 있어 코드에서 의존 객체를 바꿀 수 있다. 이로인해 의존 역전 원칙을 어기는 원인이 될 수 있다.
    
    