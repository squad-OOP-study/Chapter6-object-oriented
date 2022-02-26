# DI(Dependency Injection)와 서비스 로케이터

## [1]. 어플리케이션 영역과 메인 영역

> 어플리케이션 영역

  - 어플리케이션 이용해 구현하고자 하는 기능, 목적, 처리 등이 존재하는 영역이다.

> 메인 영역

  - 어플리케이션에서 작동할 기능, 목적, 처리 등이 정상적으로 작동하기 위한 설정 영역이다.
  
  - 어플리케이션 영역에서 사용할 객체를 생성한다.
  
  - 각 객체 간의 의존 관계를 설정하며, 어플리케이션을 실행하게 된다.

- 모든 의존은 메인 영역에서 어플리케이션 영역으로 향한다.

  반대로는 존재하지 않는다.
  
- 객체에서 필요한 객체들을 내부에서 생성하는 것이 아니라

  필요한 객체를 가져온 뒤, 원하는 기능을 실행시킬 수 있는데
  
  사용할 객체를 제공하는 책임을 갖는 객체를 서비스 로케이터(Service Locator)라고 부른다.
  
## [2]. DI을 이용한 의존 객체 사용

- 콘트리트 클래스를 직접 사용해서 객체를 생성하면

  의존 역전 원칙을 위반하게 되며, 결과적으로 확장 폐쇄 원칙을 위반한다.
  
  그로 인해, 변화에 경직된 유연하지 못한 코드를 생성하게 된다.
  
- 서비스 로케이터를 이용해서 의존 객체를 찾는 경우도 단점이 존재한다.

- 단점들을 보안한 것이 `DI(Dependency Injection; 의존 주입)` 이다.

  DI는 객체를 직접 생성하지 않고 외부에서 넣어 주는 방식이다.
  
> DI 적용을 위한 생성자 방식

  - 의존 객체를 외부에서 생성한 후,
  
    의존할 객체가 필요한 객체의 생성자의 파라미터로 받아서 주입시킨다.

  - 의존 객체를 우선적으로 생성할 수 없다면 이용할 수 없다.
 
  - 의존 객체가 누락된 경우 객체 생성하는 순간에 오류가 생성된다.

> DI 적용을 위한 설정 메서드 방식

  - 객체를 생성한 이후에 getter, setter의 setter로 의존 객체를 주입시켜준다.
  
  - 의존 객체를 누락시켜 주입하지 못하는 경우 NullPointerException이 발생할 수 있다.

## [3]. 서비스 로케이터를 이용한 의존 객체 사용

- 사용할 객체를 제공하는 책임을 갖는 객체를 서비스 로케이터(Service Locator)라고 부른다.

- 서비스 로케이터는 어플리케이션 영역의 객체에서 직접 접근하기 때문에

  메인 영역에서는 서비스 로케이터가 제공할 객체를 생성하고
  
  이 객체를 이용해 서비스 로케이터를 초기화해준다.
  
> 객체 등록 방식의 서비스 로케이터 구현

  - 서비스 로케이터를 생성할 때 사용할 객체를 전달한다.
  
  - 서비스 로케이터 인스턴스를 지정하고 참조하기 위한 static 메서드를 제공한다
   
  - 제공할 객체 종류가 많을 경우, 서비스 로케이터 객체를 생성할 때
  
  한 번에 모든 객체를 전달하는 것은 코드 가독성을 떨어뜨린다.

  그래서, setter을 이용해 초기화의 가독성을 높일 수 있다.
  
  - 구현 방식이 쉽지만,
  
  객체 등록(set) 인터페이스가 노출되어 있기 때문에

  어플리케이션 영역에서 얼마든지 의존 객체 변경이 가능하다.
  
> 상속을 통한 서비스 로케이터 구현

  - 객체를 구하는 추상 메서드를 제공하는 상위 타입 구현
  
  - 상위 타입을 상속받은 하위 타입에서 사용할 객체를 설정

> 지네릭, 템플릿을 통한 서비스 로케이터

  - 서비스 로케이터의 가장 큰 단점은 인터페이스 분리 원칙 위반이다.
  
  - 서비스 로케이터가 여러 의존 객체를 가지고 있기 때문에,
  
  단 하나만 수정해도, 모든 것이 재컴파일이 이루어지는 단점이 발생한다.

  - 각 의존 객체별로 로케이터 객체를 만들어 해결할 수 있지만,
  
  코드 중복이 발생한다. 제네릭을 이용해 코드 중복 해결이 가능하다.

> 서비스 로케이터의 단점

  - 동일한 타입의 객체가 여러 곳에서 필요하다면,

  각 객체 별도로 제공해주는 메서드를 만들어야 한다.
  
  콘크리트 클래스에서 의존하는 효과를 내는 단점이 발생한다.
  
  - 인터페이스 분리 원칙에 위배가 되며, 여러 곳에서 의존 관계가 묶이게 된다.

  
