package uz.mrx.arigo.domain.usecase.order.impl

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.data.repository.order.OrderRepository
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class OrderUseCaseImpl @Inject constructor(
    private val repository: OrderRepository
) : OrderUseCase {

    override suspend fun createOrder(id: Int, request: OrderRequest): Flow<ResultData<OrderResponse>> {
        return repository.createOrder(id, request)
    }

    override suspend fun updateOrder(
        id: Int,
        request: UpdateOrderRequest
    ): Flow<ResultData<OrderResponse>> = repository.updateOrder(id, request)

}
