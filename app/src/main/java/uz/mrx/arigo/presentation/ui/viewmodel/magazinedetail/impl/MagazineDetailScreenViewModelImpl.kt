package uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.presentation.direction.magazinedetail.MagazineDetailScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class MagazineDetailScreenViewModelImpl @Inject constructor(private val direction: MagazineDetailScreenDirection, private val useCase: FeatureUseCase):MagazineDetailScreenViewModel, ViewModel() {

    override fun openSearchDeliveryScreen() {

        viewModelScope.launch {
            direction.openSearchDeliveryScreen()
        }

    }

    override fun getFeaturesDetail(id: Int) {

        viewModelScope.launch {
            useCase.getFeaturesDetail(id).collectLatest {
                it.onSuccess {
                    featuresDetailResponse.emit(it)
                }
                it.onError {

                }
            }
        }

    }

    override val featuresDetailResponse = flow<FeatureDetailResponse>()

}