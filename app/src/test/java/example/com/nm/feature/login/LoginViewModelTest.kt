package example.com.nm.feature.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import example.com.nm.factory.DataFactory
import example.com.nm.feature.di.loginViewModelTest
import example.com.nm.feature.login.domain.LoginSource
import example.com.nm.feature.login.ui.LoginViewModel
import example.com.nm.util.TrampolineSchedulerRule
import io.reactivex.Completable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.java.standalone.KoinJavaStarter.startKoin
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException


class LoginViewModelTest  : KoinTest {

    lateinit var viewModel: LoginViewModel
    private val loginSource: LoginSource = mock()

    private var buttonData: Observer<Boolean> = mock()
    private var emailData: Observer<Boolean> = mock()
    private var uiData: Observer<LoginViewModel.ResultUIModel> = mock()
    private var loadData: Observer<Boolean> = mock()

    private val httpException: HttpException = mock()


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulers = TrampolineSchedulerRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        startKoin(listOf(loginViewModelTest))
        viewModel = LoginViewModel(loginSource)

    }

    @After
    fun after() {
        closeKoin()
    }

    @Test
    fun `check one field empty`() {
        viewModel.buttonData.observeForever(buttonData)

        val email = DataFactory.EMAIL
        val password = ""

        viewModel.checkFieldNotEmpty(email, password)

        Mockito.verify(buttonData).onChanged(false)
    }

    @Test
    fun `check all fields empty`() {
        viewModel.buttonData.observeForever(buttonData)

        val email = ""
        val password = ""

        viewModel.checkFieldNotEmpty(email, password)

        Mockito.verify(buttonData).onChanged(false)
    }

    @Test
    fun `check fields fill`() {
        viewModel.buttonData.observeForever(buttonData)

        val email = DataFactory.randomString()
        val password = DataFactory.randomString()

        viewModel.checkFieldNotEmpty(email, password)

        Mockito.verify(buttonData).onChanged(true)
    }


    @Test
    fun `check login validate false`() {
        viewModel.emailData.observeForever(emailData)

        val email = DataFactory.randomString()
        val password = DataFactory.randomString()

        viewModel.loginValidate(email, password)

        Mockito.verify(emailData).onChanged(false)
    }

    @Test
    fun `check login validate true`() {
        val username = DataFactory.EMAIL
        val password = DataFactory.randomString()

        stubLogin(username, password)

        viewModel.emailData.observeForever(emailData)
        viewModel.loadData.observeForever(loadData)
        viewModel.uiData.observeForever(uiData)

        viewModel.loginValidate(username, password)

        inOrder(emailData, loginSource, loadData, uiData) {
            verify(emailData).onChanged(true)
            verify(loginSource).login(username, password)
            verify(loadData).onChanged(true)
            verify(loadData).onChanged(false)
        }

        Mockito.verify(uiData).onChanged(LoginViewModel.ResultUIModel(true, null))

    }

    @Test
    fun `check login error`() {
        val username = DataFactory.EMAIL
        val password = DataFactory.randomString()

        stubLoginError(username, password)

        viewModel.emailData.observeForever(emailData)
        viewModel.loadData.observeForever(loadData)
        viewModel.uiData.observeForever(uiData)

        viewModel.loginValidate(username, password)

        inOrder(emailData, loginSource, loadData, uiData) {
            verify(emailData).onChanged(true)
            verify(loginSource).login(username, password)
            verify(loadData).onChanged(true)
            verify(loadData).onChanged(false)
        }

        Mockito.verify(uiData).onChanged(LoginViewModel.ResultUIModel(false, null))
    }

    private fun stubLogin(username: String, password: String) {
        whenever(loginSource.login(username, password)).thenReturn(Completable.complete())
    }

    private fun stubLoginError(username: String, password: String) {
        whenever(loginSource.login(username, password)).thenReturn(Completable.error(httpException))
    }

}