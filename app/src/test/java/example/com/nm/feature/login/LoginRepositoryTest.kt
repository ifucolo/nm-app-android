package example.com.nm.feature.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import example.com.nm.factory.DataFactory
import example.com.nm.feature.api.ServerApi
import example.com.nm.feature.login.repository.LoginRepository
import example.com.nm.feature.pref.NmPref
import example.com.nm.util.FilesFromTestResources.getJson
import example.com.nm.util.TrampolineSchedulerRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class LoginRepositoryTest  : KoinTest {


    private lateinit var server: MockWebServer
    private lateinit var repository: LoginRepository
    private val nmPref: NmPref = mock()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulers = TrampolineSchedulerRule()

    @Before
    fun beforeEachTest() {
        server = MockWebServer()

        val serverApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(server.url("/").toString())
            .build()
            .create(ServerApi::class.java)

        repository = LoginRepository(serverApi, nmPref)
    }

    @After
    fun afterEachTest() {
        server.shutdown()
    }


    @Test
    fun `check valid user`() {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(getJson("login/user_registered"))
        )

        val email = DataFactory.EMAIL
        val password = DataFactory.randomString()

        repository.login(email, password)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

}