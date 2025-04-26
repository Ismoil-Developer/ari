package uz.mrx.arigo.presentation.ui.viewmodel.shop.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.presentation.direction.location.SearchMapScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.shop.SearchMapScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class SearchMapScreenViewModelImpl @Inject constructor(private val direction:SearchMapScreenDirection, private val useCase: FeatureUseCase):SearchMapScreenViewModel, ViewModel() {

    override fun getMapList(id: Int,radius:Int) {
        viewModelScope.launch {
            useCase.getMapList(id, radius).collectLatest {
                it.onSuccess {
                    searchMapListResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override val searchMapListResponse = flow<List<MapListResponse>>()

    override fun openShopDetail(id: Int) {
        viewModelScope.launch {
            direction.openSearchMapScreen(id)
        }
    }


}