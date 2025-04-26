package uz.mrx.arigo.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uz.mrx.arigo.utils.MagazineImage

class MagazineIndicatorAdapter(var imageList: ArrayList<Int>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return MagazineImage.newInstance(imageList[position])
    }

    override fun getCount(): Int = imageList.size

}