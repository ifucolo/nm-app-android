package example.com.nm.feature.splash

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import example.com.nm.feature.di.splashViewModelTest
import example.com.nm.feature.di.testModule
import org.junit.*
import org.koin.java.standalone.KoinJavaStarter.startKoin
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SplashViewModelTest : KoinTest {

    private val viewModel: SplashViewModel by inject()
    private var hasToken: Observer<Boolean> = mock()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        startKoin(listOf(splashViewModelTest))
    }

    @After
    fun after() {
        closeKoin()
    }

    @Test
    fun `test has not token`() {
        viewModel.hasToken.observeForever(hasToken)

        viewModel.checkToken()

        Assert.assertNotNull(viewModel.hasToken.value)

        Mockito.verify(hasToken).onChanged(false)
    }

    @Test
    fun `test has token`() {
        viewModel.hasToken.observeForever(hasToken)

        viewModel.hasToken.value = true
        viewModel.checkToken()

        Assert.assertNotNull(viewModel.hasToken.value)

        Mockito.verify(hasToken).onChanged(true)
    }
}