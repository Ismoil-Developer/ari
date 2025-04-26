package uz.mrx.arigo.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ItemMagazineImageBinding

private const val ARG_PARAM1 = "param1"


class MagazineImage: Fragment(R.layout.item_magazine_image) {
    private var param1: Int? = null
    private val binding: ItemMagazineImageBinding by viewBinding(ItemMagazineImageBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }

        binding.magazineImageViewPager2.setImageResource(param1!!)

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            MagazineImage().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}