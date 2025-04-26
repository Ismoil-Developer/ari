package uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse

interface MagazineDetailScreenViewModel {

    fun openSearchDeliveryScreen()

    fun getFeaturesDetail(id:Int)

    val featuresDetailResponse:Flow<FeatureDetailResponse>

}