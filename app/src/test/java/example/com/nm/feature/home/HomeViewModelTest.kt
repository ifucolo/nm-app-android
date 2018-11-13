package example.com.nm.feature.home

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import example.com.nm.factory.DataFactory
import example.com.nm.feature.home.domain.HomeSource
import example.com.nm.feature.home.domain.entity.UserData
import example.com.nm.feature.home.ui.HomeViewModel
import example.com.nm.feature.pref.NmPref
import example.com.nm.util.TrampolineSchedulerRule
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.HttpException

class HomeViewModelTest : KoinTest {

    private lateinit var viewModel: HomeViewModel

    private var uiData: Observer<HomeViewModel.ResultUIModel> = mock()
    private val homeSource: HomeSource = mock()
    private val httpException: HttpException = mock()
    private var loadData: Observer<Boolean> = mock()
    private var nmPref: NmPref = mock()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulers = TrampolineSchedulerRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        viewModel = HomeViewModel(homeSource)
    }

    @Test
    fun `check user info valid`() {
        viewModel.uiData.observeForever(uiData)
        viewModel.loadData.observeForever(loadData)

        val userData = stubUserData()
        stubGetUserInfo(userData)

        viewModel.getUserInfo()

        inOrder(homeSource, loadData) {
            verify(homeSource).getUserInfo()
            verify(loadData).onChanged(true)
            verify(loadData).onChanged(false)
        }

        Mockito.verify(uiData).onChanged(HomeViewModel.ResultUIModel(userData, null))
    }

    @Test
    fun `check error user info`() {
        viewModel.uiData.observeForever(uiData)
        viewModel.loadData.observeForever(loadData)

        stubGetUserInfoError()

        viewModel.getUserInfo()

        inOrder(homeSource, loadData) {
            verify(homeSource).getUserInfo()
            verify(loadData).onChanged(true)
            verify(loadData).onChanged(false)
        }

        Mockito.verify(uiData).onChanged(HomeViewModel.ResultUIModel(null, httpException))
    }

    @Test fun `check clean user info`() {
        viewModel.cleanUserData()

        nmPref.clean()
        Mockito.verify(nmPref).clean()
    }

    private fun stubGetUserInfo(userData: UserData) {
        whenever(homeSource.getUserInfo()).thenReturn(Single.just(userData))
    }

    private fun stubGetUserInfoError() {
        whenever(homeSource.getUserInfo()).thenReturn(Single.error(httpException))
    }

    private fun stubUserData() = UserData(
        email = DataFactory.randomString(),
        country = DataFactory.randomString(),
        lastName = DataFactory.randomString(),
        firstName = DataFactory.randomString(),
        status = DataFactory.randomString()
    )
}