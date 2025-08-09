package uz.mrx.arigo.presentation.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine.ListPage
import uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine.MagazineInfoPage

class MagazineViewPager(fm: FragmentActivity, private val id:Int, private val roleId:Int) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListPage(id, roleId)
            1 -> MagazineInfoPage(id)
            else -> ListPage(id, roleId)
        }
    }

}