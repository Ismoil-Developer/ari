package uz.mrx.arigo.presentation.ui.screen.fragment.auth.privacy

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenPrivacyBinding
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class PrivacyScreen : Fragment(R.layout.screen_privacy) {

    private val binding: ScreenPrivacyBinding by viewBinding(ScreenPrivacyBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // PDFni assets'dan cache'ga ko'chiramiz
        val inputStream = requireContext().assets.open("offerta.pdf")

        val outFile = File(requireContext().cacheDir, "offerta.pdf")

        FileOutputStream(outFile).use { output ->
            inputStream.copyTo(output)
        }

        // PdfRenderer bilan PDF sahifalarini chiqaramiz
        val fileDescriptor = ParcelFileDescriptor.open(outFile, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(fileDescriptor)

        for (i in 0 until renderer.pageCount) {
            val page = renderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            val imageView = ImageView(requireContext())
            imageView.setImageBitmap(bitmap)
            imageView.adjustViewBounds = true
            binding.textOfferta.addView(imageView)

            page.close()

        }

        renderer.close()

        // Toolbar back bosilganda orqaga qaytish
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }
}
