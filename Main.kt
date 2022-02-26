/*

// Locator 사용 전
class Worker {

    fun run() {
        val jobQueue: JobQueue = JobQueue 클래스의 콘크리트 클래스 중 하나 // JobQueue를 구한다
        val transcoder: Transcoder = Transcoder 클래스의 콘크리트 클래스 중 하나 // Transcoder를 구한다

        while (someRunningCondition) {
            val jobData: JobData = jobQueue.getJob()
            transcoder.transcode(jobData.getSource(), jobData getTarget())
        }
    }
}

class JobCLI {

    fun interact() {
        printInputSourceMessage()
        val source = getSourceFromConsole()
        printInputTargetMessage()
        val target = getTargetFromConsole()

        val jobQueue: JobQueue = JobQueue 클래스의 콘크리트 클래스 중 하나 // JobQueue를 구한다
        jobQueue.addJob(JobData(source, target))
    }
}

// Locator 사용 후

class Worker {

    fun run() {
        val jobQueue: JobQueue  = Locator.getInstance().getJobQueue() // Locator를 통해 JobQueue 콘크리트 클래스를 구함
        val transcoder: Transcoder = Locator.getInstance().getTranscorder() // Locator를 통해 TransCoder 콘크리트 클래스를 구함

        while (someRunningCondition) {
            ...
        }
    }
}

class JobCLI {

    fun interact() {
        printInputSourceMessage()
        val source = getSourceFromConsole()
        printInputTargetMessage()
        val target = getTargetFromConsole()

        val jobQueue: JobQueue = Locator.getInstance().getJobQueue() // Locator를 통해 jobqueue 콘크리트 클래스를 구함
        jobQueue.addJob(JobData(source, target))
    }
}

// Locator 코드 구성

class Locator {

    companion object {
        private var instance: Locator? = null

        fun getInstance() = instance

        fun init(locator: Locator) {
            this.instance = locator
        }
    }

    private val jobQueue : JobQueue
    private val transcoder : TransCoder

    constructor(jobQueue : JobQueue, transcoder : TransCoder) {
        this.jobQueue = jobQueue
        this.transcoder = transcoder
    }

    fun getJobQueue() = jobQueue
    fun getTranscoder() = transcoder

}

/// 메인 영역 코드 구성

fun main(args: Array<String>) {
    // 상위 수준 모듈인 trasncoder 패키지에서 사용할
    // 하위 수준 모듈 객체 생성
    val jobQueue: JobQueue = FileJobQueue()
    val transcoder: TransCoder = FfmpegTransCoder()

    // 상위 수준 모듈이 하위 수준 모듈을 사용할 수 있도록 Locator 초기화
    val locator: Locator = Locator(jobQueue, transcoder)
    Locator.init(locator)

    // 상위 수준 모듈 객체를 생성하고 실행
    val worker: Worker = Worker()
    val t = Thread(runnable) {
        fun run() {
            worker.fun()
        }
    }
    val cli = JobCLI()
    cli.interact()

}

class JobQueue {}
class TransCoder {}


// DI 사용(생성자 구현)

class Worker(private val jobQueue: JobQueue, private val transcoder: TransCoder) {

    fun run() {

        while (someRunningCondition) {
            ...
        }
    }
}

class JobCLI(private val jobQueue: JobQueue) {

    fun interact() {
        printInputSourceMessage()
        val source = getSourceFromConsole()
        printInputTargetMessage()
        val target = getTargetFromConsole()

        jobQueue.addJob(JobData(source, target))
    }
}

fun main(args: Array<String>) {
    // 상위 수준 모듈인 trasncoder 패키지에서 사용할
    // 하위 수준 모듈 객체 생성
    val jobQueue: JobQueue = FileJobQueue()
    val transcoder: TransCoder = FfmpegTransCoder()

    // 상위 수준 모듈 객체를 생성하고 실행
    val worker: Worker = Worker(jobQueue, transcoder)
    val t = Thread(runnable) {
        fun run() {
            worker.fun()
        }
    }
    val cli = JobCLI(jobQueue)
    cli.interact()

}


// 조립기 객체 사용

class Assembler {
    private lateinit var worker: Worker
    private lateinit var jobCLI: JobCLI

    fun createAndWire() {
        val jobQueue: JobQueue = FileJobQueue()
        val transcoder: TransCoder = FfmpegTransCoder()
        worker = Worker(jobQueue, transcoder)
        jobCLI = JobCLI(jobQueue)
    }

    fun getWorker() = this.worker
    fun getJobCLI() = this.jobCLI
    ...
}

fun main(args: Array<String>) {
    val assembler = Assembler()
    assembler.createAndWire()
    val worker: Worker = assembler.getWorker()
    val jobCli = assembler.getJobCLI()
    ...

}

// 설정 메서드 방식 구현

class Worker() {
    private lateinit var jobQueue: JobQueue
    private lateinit var transcoder: TransCoder

    fun setJobQueue(jobQueue: JobQueue) {
        this.jobQueue = jobQueue
    }

    fun setTranscoder(transcoder: TransCoder) {
        this.transcoder = this.transcoder
    }

    fun run() {
        while (someRunningCondition) {
            val jobData = jobQueue.getJob()
            transcoder.transcode(jobData.getSource(), jobData.getTarget())
        }
    }
}

// 다양한 방식의 설정 메서드
// 한 개의 메서드로 의존 객체 모두 설정
fun configure(jobQueue: JobQueue, transcoder: TransCoder) {
    this.jobQueue = jobQueue
    this.transcoder = this.transcoder
}

// 메서드 체이닝이 가능하도록 리턴 타입을 객체로 설정
fun setJobQueue(jobQueue: JobQueue): Worker {
    this.jobQueue = jobQueue
    return this
}

fun setTranscoder(transcoder: TransCoder): Worker {
    this.transcoder = this.transcoder
    return this
}
val worker = Worker()
worker.setJobQueue(FileJobQueue()).setTranscoder(FfmpegTransCoder()).run()

// 서비스 로케이터 companion object 있다

class ServiceLocator {
    //서비스 로케이터 접근을 위한 companion object
    companion object {
        private var instance: ServiceLocator? = null

        fun getInstance() = instance

        fun load(locator: ServiceLocator) {
            ServiceLocator.instance = locator
        }
    }

    private val jobQueue : JobQueue
    private val transcoder : TransCoder

    constructor(jobQueue : JobQueue, transcoder : TransCoder) {
        this.jobQueue = jobQueue
        this.transcoder = transcoder
    }

    fun getJobQueue() = jobQueue
    fun getTranscoder() = transcoder

}

fun main(args: Array<String>) {
    // 의존 객체 생성
    val jobQueue: JobQueue = FileJobQueue()
    val transcoder: TransCoder = FfmpegTransCoder()

    // 서비스 로케이터 초기화
    val locator: ServiceLocator = ServiceLocator(jobQueue, transcoder)
    ServiceLocator.load(locator)

    // 어플리케이션 코드 실행
    val worker: Worker = Worker()
    val cli = JobCLI()
    cli.interact()
}

class Worker {
    fun run() {
        // 서비스 로케이터 이용해서 의존 객체 구함
        val locator = ServiceLocator.getInstance()
        val jobQueue: JobQueue  = locator.getJobQueue()
        val transcoder: Transcoder = locator.getTranscorder()

        // 의존 객체 사용
        while (someRunningCondition) {
            val jobData = jobQueue.getJob()
            transcoder.transcode(jobData.getSource(), jobData.getTarget())
        }
    }
}

// 서비스 로케이터 companion object 없다

class ServiceLocator {
    private val jobQueue : JobQueue
    private val transcoder : TransCoder

    constructor(jobQueue : JobQueue, transcoder : TransCoder) {
        this.jobQueue = jobQueue
        this.transcoder = transcoder
    }

    fun getJobQueue() = jobQueue
    fun getTranscoder() = transcoder

}

fun main(args: Array<String>) {
    // 의존 객체 생성
    val jobQueue: JobQueue = FileJobQueue()
    val transcoder: TransCoder = FfmpegTransCoder()

    // 서비스 로케이터 초기화
    val locator: ServiceLocator = ServiceLocator(jobQueue, transcoder)
//    ServiceLocator.load(locator)

    // 어플리케이션 코드 실행
    val worker: Worker = Worker()
    val cli = JobCLI()
    cli.interact()
}

class Worker {
    fun run() {
        val jobQueueTest: JobQueue = FileJobQueue()
        val transcoderTest: TransCoder = FfmpegTransCoder()
        val locator = ServiceLocator(jobQueueTest, transcoderTest)
        val jobQueue: JobQueue  = locator.getJobQueue()
        val transcoder: Transcoder = locator.getTranscorder()

        // 의존 객체 사용
        while (someRunningCondition) {
            val jobData = jobQueue.getJob()
            transcoder.transcode(jobData.getSource(), jobData.getTarget())
        }
    }
}
*/
