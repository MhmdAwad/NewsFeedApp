package com.example.newsfeedapp.ui.fragment.details


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsfeedapp.R
import com.example.newsfeedapp.common.dateFormat
import com.example.newsfeedapp.common.dateToTimeFormat
import com.example.newsfeedapp.common.showToast
import com.example.newsfeedapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val args: DetailsFragmentArgs by navArgs()

    @Inject
    lateinit var glide: RequestManager

    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
        favBtn.isFavorite = args.article.isFav == 1

        favBtn.setOnFavoriteChangeListener { buttonView, favorite ->
            lifecycleScope.launch(Dispatchers.Main) {
                Log.e("TAG", "on fav clicked" + favorite)


                if (favorite)
                    viewModel.updateFavorite(1, args.article.url)
                else
                    viewModel.updateFavorite(0, args.article.url)


            }
        }

        shareBtn.setOnClickListener {
            try {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plan"
                i.putExtra(Intent.EXTRA_SUBJECT, args.article.title)
                val body: String =
                    args.article.title.toString() + "\n" + args.article.url + "\n" + "Share from the News App" + "\n"
                i.putExtra(Intent.EXTRA_TEXT, body)
                startActivity(Intent.createChooser(i, "Share with :"))
            } catch (e: Exception) {
                showToast("Sorry, \nCannot be share")
            }
        }

        openWebSite.setOnClickListener {
            val action = args.article.url.let { url ->
                DetailsFragmentDirections.actionDetailsFragmentToWebViewFragment(url)
            }
            action.let { action -> findNavController().navigate(action) }
        }
    }

    private fun bindData() {
        glide.load(args.article.urlToImage)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(articleImage)



        titleTxt.text = args.article.title
        authorNameTxt.text = args.article.author
        dateTxt.text = dateFormat(args.article.publishedAt)
        articleTimeAgo.text = dateToTimeFormat(args.article.publishedAt)
        descriptionTxt.text = args.article.description
    }

}

