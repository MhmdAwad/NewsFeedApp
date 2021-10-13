package com.example.newsfeedapp.ui.fragment.wish_list

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsfeedapp.R
import com.example.newsfeedapp.common.*
import com.example.newsfeedapp.data.model.Article
import com.example.newsfeedapp.ui.NewsViewModel
import com.example.newsfeedapp.ui.adapter.NewsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_wish_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint

class WishListFragment : Fragment(R.layout.fragment_wish_list), NewsAdapter.Interaction,
    SearchView.OnQueryTextListener {

    private val newsAdapter by lazy { NewsAdapter(this) }
    private lateinit var favList: MutableList<Article>
    private val viewModel: NewsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favList = mutableListOf()
        setHasOptionsMenu(true)
        viewModel.getHomeNews(false)
        setupRecyclerView()
        observeToFavLiveData()
        swipeToDelete(view)
    }

    private fun swipeToDelete(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                iewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                lifecycleScope.launch(Dispatchers.IO) {
                    Log.e("TAG", "delete all")



                    viewModel.updateFavorite(0, article.url)
                    favList.remove(article)

                    viewModel.getHomeNews(false)

                }

                Snackbar.make(view, getString(R.string.deleteArticle), Snackbar.LENGTH_LONG).apply {
                    setAction(getString(R.string.undo)) {

                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.updateFavorite(1, article.url)
                            viewModel.getHomeNews(false)
                        }

                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(favNewsRecycler)
        }
    }

    private fun observeToFavLiveData() {
        viewModel.getNews().observe(viewLifecycleOwner, Observer { articles ->


            when (articles) {
                is Resource.Error -> {
                    ProgressBar_wishList.gone()
                }

                is Resource.Loading -> ProgressBar_wishList.show()
                is Resource.Success -> {
                    ProgressBar_wishList.gone()
                    val filteredList = articles.data?.filter {
                        it.isFav == 1
                    };
                    newsAdapter.differ.submitList(filteredList?.reversed())
                    filteredList?.let { favList.addAll(it?.reversed()) }
                }
            }


        }

        )


    }

    private fun setupRecyclerView() {
        favNewsRecycler.apply {
            adapter = newsAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Article) {
        val action = WishListFragmentDirections.actionNavWishListToDetailsFragment(item)
        findNavController().navigate(action)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_deleteAll -> {

                if (favList.isNotEmpty())
                    showDialog(
                        getString(R.string.deleteAll),
                        getString(R.string.yes),
                        { dialog, which ->

                            lifecycleScope.launch(Dispatchers.IO) {
                                Log.e("TAG", "delete all")

                                val size = favList.size - 1

                                for (i in 0..size) {
                                    viewModel.updateFavorite(0, favList[i].url)
                                }
                                viewModel.getHomeNews(false)

                            }


                        },
                        getString(R.string.no),
                        { dialog, which ->
                            dialog.dismiss()
                        },
                        true
                    )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fav_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        onQueryTextChange(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newsAdapter.differ.submitList(searchQuery(newText, favList))
        return true
    }
}
