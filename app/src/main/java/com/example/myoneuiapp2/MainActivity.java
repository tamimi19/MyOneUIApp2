وجدت هذه الملفات على هذا المشروع.

https://github.com/OneUIProject/oneui-design?tab=readme-ov-file



sample-app/src/main/java/dev/oneuiproject/oneuiexample/fragment/IndexScrollFragment.java



package dev.oneuiproject.oneuiexample.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.util.SeslRoundedCorner;
import androidx.appcompat.util.SeslSubheaderRoundedCorner;
import androidx.appcompat.view.menu.SeslMenuItem;
import androidx.indexscroll.widget.SeslCursorIndexer;
import androidx.indexscroll.widget.SeslIndexScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sec.sesl.tester.R;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.utils.IndexScrollUtils;
import dev.oneuiproject.oneui.widget.Separator;
import dev.oneuiproject.oneuiexample.base.BaseFragment;

public class IndexScrollFragment extends BaseFragment {
    private int mCurrentSectionIndex = 0;
    private RecyclerView mListView;
    private SeslIndexScrollView mIndexScrollView;

    private boolean mIsTextModeEnabled = false;
    private boolean mIsIndexBarPressed = false;
    private final Runnable mHideIndexBar = new Runnable() {
        @Override
        public void run() {
            IndexScrollUtils.animateVisibility(mIndexScrollView, false);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIndexScrollView = view.findViewById(R.id.indexscroll_view);
        initListView(view);
        initIndexScroll();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem textModeItem = menu.findItem(R.id.menu_indexscroll_text);
        textModeItem.setVisible(true);
        if (mIsTextModeEnabled) {
            textModeItem.setTitle("Hide letters");
        } else {
            textModeItem.setTitle("Show letters");
        }
        ((SeslMenuItem) textModeItem)
                .setBadgeText(getString(R.string.oui_new_badge_text));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_indexscroll_text) {
            ((SeslMenuItem) item)
                    .setBadgeText(null);

            mIsTextModeEnabled = !mIsTextModeEnabled;
            if (mIsTextModeEnabled) {
                item.setTitle("Hide letters");
            } else {
                item.setTitle("Show letters");
            }

            mIndexScrollView.setIndexBarTextMode(mIsTextModeEnabled);
            mIndexScrollView.invalidate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        final boolean isRtl = newConfig
                .getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        if (mIndexScrollView != null) {
            mIndexScrollView.setIndexBarGravity(isRtl
                    ? SeslIndexScrollView.GRAVITY_INDEX_BAR_LEFT
                    : SeslIndexScrollView.GRAVITY_INDEX_BAR_RIGHT);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.sample3_fragment_indexscroll;
    }

    @Override
    public int getIconResId() {
        return R.drawable.ic_oui_edge_panels;
    }

    @Override
    public CharSequence getTitle() {
        return "IndexScroll";
    }

    private void initListView(@NonNull View view) {
        mListView = view.findViewById(R.id.indexscroll_list);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setAdapter(new IndexAdapter());
        mListView.addItemDecoration(new ItemDecoration(mContext));
        mListView.setItemAnimator(null);
        mListView.seslSetFillBottomEnabled(true);
        mListView.seslSetLastRoundedCorner(true);
        mListView.seslSetIndexTipEnabled(true);
        mListView.seslSetGoToTopEnabled(true);
        mListView.seslSetSmoothScrollEnabled(true);
    }

    private void initIndexScroll() {
        final boolean isRtl = getResources().getConfiguration()
                .getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;

        mIndexScrollView.setIndexBarGravity(isRtl
                ? SeslIndexScrollView.GRAVITY_INDEX_BAR_LEFT
                : SeslIndexScrollView.GRAVITY_INDEX_BAR_RIGHT);

        MatrixCursor cursor = new MatrixCursor(new String[]{"item"});
        for (String item : listItems) {
            cursor.addRow(new String[]{item});
        }

        cursor.moveToFirst();

        SeslCursorIndexer indexer = new SeslCursorIndexer(cursor, 0,
                "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,Б".split(","), 0);
        indexer.setGroupItemsCount(1);
        indexer.setMiscItemsCount(3);

        mIndexScrollView.setIndexer(indexer);
        mIndexScrollView.setOnIndexBarEventListener(
                new SeslIndexScrollView.OnIndexBarEventListener() {
                    @Override
                    public void onIndexChanged(int sectionIndex) {
                        if (mCurrentSectionIndex != sectionIndex) {
                            mCurrentSectionIndex = sectionIndex;
                            if (mListView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                                mListView.stopScroll();
                            }
                            ((LinearLayoutManager) mListView.getLayoutManager())
                                    .scrollToPositionWithOffset(sectionIndex, 0);
                        }
                    }

                    @Override
                    public void onPressed(float v) {
                        mIsIndexBarPressed = true;
                        mListView.removeCallbacks(mHideIndexBar);
                    }

                    @Override
                    public void onReleased(float v) {
                        mIsIndexBarPressed = false;
                        if (mListView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                            mListView.postDelayed(mHideIndexBar, 1500);
                        }
                    }
                });
        mIndexScrollView.attachToRecyclerView(mListView);
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && !mIsIndexBarPressed) {
                    recyclerView.postDelayed(mHideIndexBar, 1500);
                } else {
                    mListView.removeCallbacks(mHideIndexBar);
                    IndexScrollUtils.animateVisibility(mIndexScrollView, true);
                }
            }
        });
    }

    public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder>
            implements SectionIndexer {
        List<String> mSections = new ArrayList<>();
        List<Integer> mPositionForSection = new ArrayList<>();
        List<Integer> mSectionForPosition = new ArrayList<>();

        IndexAdapter() {
            mSections.add("");
            mPositionForSection.add(0);
            mSectionForPosition.add(0);

            for (int i = 1; i < listItems.length; i++) {
                String letter = listItems[i];
                if (letter.length() == 1) {
                    mSections.add(letter);
                    mPositionForSection.add(i);
                }
                mSectionForPosition.add(mSections.size() - 1);
            }
        }

        @Override
        public int getItemCount() {
            return listItems.length;
        }

        @Override
        public int getItemViewType(int position) {
            return (listItems[position].length() == 1) ? 1 : 0;
        }

        @NonNull
        @Override
        public IndexAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View view = inflater.inflate(
                        R.layout.sample3_view_indexscroll_listview_item, parent, false);
                return new IndexAdapter.ViewHolder(view, false);
            } else {
                return new IndexAdapter.ViewHolder(new Separator(mContext), true);
            }
        }

        @Override
        public void onBindViewHolder(IndexAdapter.ViewHolder holder, final int position) {
            if (holder.isSeparator) {
                holder.textView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else {
                if (position == 0) {
                    holder.imageView.setImageResource(R.drawable.indexscroll_group_icon);

                } else {
                    holder.imageView.setImageResource(R.drawable.indexscroll_item_icon);

                }
            }
            holder.textView.setText(listItems[position]);
        }

        @Override
        public Object[] getSections() {
            return mSections.toArray();
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            return mPositionForSection.get(sectionIndex);
        }

        @Override
        public int getSectionForPosition(int position) {
            return mSectionForPosition.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            boolean isSeparator;
            ImageView imageView;
            TextView textView;

            ViewHolder(View itemView, boolean isSeparator) {
                super(itemView);
                this.isSeparator = isSeparator;
                if (isSeparator) {
                    textView = (TextView) itemView;
                } else {
                    imageView = itemView.findViewById(R.id.indexscroll_list_item_icon);
                    textView = itemView.findViewById(R.id.indexscroll_list_item_text);
                }
            }
        }
    }

    private class ItemDecoration extends RecyclerView.ItemDecoration {
        private final Drawable mDivider;
        private final SeslSubheaderRoundedCorner mRoundedCorner;

        public ItemDecoration(@NonNull Context context) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.isLightTheme, outValue, true);

            mDivider = context.getDrawable(outValue.data == 0
                    ? R.drawable.sesl_list_divider_dark
                    : R.drawable.sesl_list_divider_light);

            mRoundedCorner = new SeslSubheaderRoundedCorner(mContext);
            mRoundedCorner.setRoundedCorners(SeslRoundedCorner.ROUNDED_CORNER_ALL);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);

            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                IndexAdapter.ViewHolder holder
                        = (IndexAdapter.ViewHolder) mListView.getChildViewHolder(child);
                if (!holder.isSeparator) {
                    final int top = child.getBottom()
                            + ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).bottomMargin;
                    final int bottom = mDivider.getIntrinsicHeight() + top;

                    mDivider.setBounds(parent.getLeft(), top, parent.getRight(), bottom);
                    mDivider.draw(c);
                }
            }
        }

        @Override
        public void seslOnDispatchDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                IndexAdapter.ViewHolder holder
                        = (IndexAdapter.ViewHolder) mListView.getChildViewHolder(child);
                if (holder.isSeparator) {
                    mRoundedCorner.drawRoundedCorner(child, c);
                }
            }
        }
    }


    String[] listItems = {
            "Groups",
            "A",
            "Aaron",
            "Abe",
            "Abigail",
            "Abraham",
            "Ace",
            "Adelaide",
            "Adele",
            "Aiden",
            "Alice",
            "Allison",
            "Amelia",
            "Amity",
            "Anise",
            "Ann",
            "Annabel",
            "Anneliese",
            "Annora",
            "Anthony",
            "Apollo",
            "Arden",
            "Arthur",
            "Aryn",
            "Ashten",
            "Avery",
            "B",
            "Bailee",
            "Bailey",
            "Beck",
            "Benjamin",
            "Berlynn",
            "Bernice",
            "Bianca",
            "Blair",
            "Blaise",
            "Blake",
            "Blanche",
            "Blayne",
            "Bram",
            "Brandt",
            "Bree",
            "Breean",
            "Brendon",
            "Brett",
            "Brighton",
            "Brock",
            "Brooke",
            "Byron",
            "C",
            "Caleb",
            "Cameron",
            "Candice",
            "Caprice",
            "Carelyn",
            "Caren",
            "Carleen",
            "Carlen",
            "Carmden",
            "Cash",
            "Caylen",
            "Cerise",
            "Charles",
            "Chase",
            "Clark",
            "Claude",
            "Claudia",
            "Clelia",
            "Clementine",
            "Cody",
            "Conrad",
            "Coralie",
            "Coreen",
            "Coy",
            "D",
            "Damien",
            "Damon",
            "Daniel",
            "Dante",
            "Dash",
            "David",
            "Dawn",
            "Dean",
            "Debree",
            "Denise",
            "Denver",
            "Devon",
            "Dex",
            "Dezi",
            "Dominick",
            "Doran",
            "Drake",
            "Drew",
            "Dustin",
            "E",
            "Edward",
            "Elein",
            "Eli",
            "Elias",
            "Elijah",
            "Ellen",
            "Ellice",
            "Ellison",
            "Ellory",
            "Elodie",
            "Eloise",
            "Emeline",
            "Emerson",
            "Eminem",
            "Erin",
            "Evelyn",
            "Everett",
            "Evony",
            "F",
            "Fawn",
            "Felix",
            "Fern",
            "Fernando",
            "Finn",
            "Francis",
            "G",
            "Gabriel",
            "Garrison",
            "Gavin",
            "George",
            "Georgina",
            "Gillian",
            "Glenn",
            "Grant",
            "Gregory",
            "Grey",
            "Gwendolen",
            "H",
            "Haiden",
            "Harriet",
            "Harrison",
            "Heath",
            "Henry",
            "Hollyn",
            "Homer",
            "Hope",
            "Hugh",
            "Hyrum",
            "I",
            "Imogen",
            "Irene",
            "Isaac",
            "Isaiah",
            "J",
            "Jack",
            "Jacklyn",
            "Jackson",
            "Jae",
            "Jaidyn",
            "James",
            "Jane",
            "Janetta",
            "Jared",
            "Jasper",
            "Javan",
            "Jax",
            "Jeremy",
            "Joan",
            "Joanna",
            "Jolee",
            "Jordon",
            "Joseph",
            "Josiah",
            "Juan",
            "Judd",
            "Jude",
            "Julian",
            "Juliet",
            "Julina",
            "June",
            "Justice",
            "Justin",
            "K",
            "Kae",
            "Kai",
            "Kaitlin",
            "Kalan",
            "Karilyn",
            "Kate",
            "Kathryn",
            "Kent",
            "Kingston",
            "Korin",
            "Krystan",
            "Kylie",
            "L",
            "Lane",
            "Lashon",
            "Lawrence",
            "Lee",
            "Leo",
            "Leonie",
            "Levi",
            "Lilibeth",
            "Lillian",
            "Linnea",
            "Louis",
            "Louisa",
            "Love",
            "Lucinda",
            "Luke",
            "Lydon",
            "Lynn",
            "M",
            "Madeleine",
            "Madisen",
            "Mae",
            "Malachi",
            "Marcella",
            "Marcellus",
            "Marguerite",
            "Matilda",
            "Matteo",
            "Meaghan",
            "Merle",
            "Michael",
            "Menime",
            "Mirabel",
            "Miranda",
            "Miriam",
            "Monteen",
            "Murphy",
            "Myron",
            "N",
            "Nadeen",
            "Naomi",
            "Natalie",
            "Naveen",
            "Neil",
            "Nevin",
            "Nicolas",
            "Noah",
            "Noel",
            "O",
            "Ocean",
            "Olive",
            "Oliver",
            "Oren",
            "Orlando",
            "Oscar",
            "P",
            "Paul",
            "Payten",
            "Porter",
            "Preston",
            "Q",
            "Quintin",
            "R",
            "Raine",
            "Randall",
            "Raven",
            "Ray",
            "Rayleen",
            "Reagan",
            "Rebecca",
            "Reese",
            "Reeve",
            "Rene",
            "Rhett",
            "Ricardo",
            "Riley",
            "Robert",
            "Robin",
            "Rory",
            "Rosalind",
            "Rose",
            "Ryder",
            "Rylie",
            "S",
            "Salvo :)",
            "Sean",
            "Selene",
            "Seth",
            "Shane",
            "Sharon",
            "Sheridan",
            "Sherleen",
            "Silvia",
            "Sophia",
            "Sue",
            "Sullivan",
            "Susannah",
            "Sutton",
            "Suzan",
            "Syllable",
            "T",
            "Tanner",
            "Tavian",
            "Taye",
            "Taylore",
            "Thomas",
            "Timothy",
            "Tobias",
            "Trevor",
            "Trey",
            "Tristan",
            "Troy",
            "Tyson",
            "U",
            "Ulvi",
            "Uwu",
            "V",
            "Vanessa",
            "Varian",
            "Verena",
            "Vernon",
            "Vincent",
            "Viola",
            "Vivian",
            "W",
            "Wade",
            "Warren",
            "Will",
            "William",
            "X",
            "Xavier",
            "Y",
            "Yann :)",
            "Z",
            "Zachary",
            "Zane",
            "Zion",
            "Zoe",
            "Б",
            "Блять lol",
            "#",
            "040404",
            "121002"
    };
}








sample-app/src/main/java/dev/oneuiproject/oneuiexample/activity/MainActivity.java

package dev.oneuiproject.oneuiexample.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sec.sesl.tester.R;
import com.sec.sesl.tester.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.utils.ActivityUtils;
import dev.oneuiproject.oneui.widget.TipPopup;
import dev.oneuiproject.oneuiexample.base.FragmentInfo;
import dev.oneuiproject.oneuiexample.fragment.AppPickerFragment;
import dev.oneuiproject.oneuiexample.fragment.IconsFragment;
import dev.oneuiproject.oneuiexample.fragment.IndexScrollFragment;
import dev.oneuiproject.oneuiexample.fragment.PickersFragment;
import dev.oneuiproject.oneuiexample.fragment.PreferencesFragment;
import dev.oneuiproject.oneuiexample.fragment.ProgressBarFragment;
import dev.oneuiproject.oneuiexample.fragment.QRCodeFragment;
import dev.oneuiproject.oneuiexample.fragment.SeekBarFragment;
import dev.oneuiproject.oneuiexample.fragment.SwipeRefreshFragment;
import dev.oneuiproject.oneuiexample.fragment.TabsFragment;
import dev.oneuiproject.oneuiexample.fragment.WidgetsFragment;
import dev.oneuiproject.oneuiexample.ui.drawer.DrawerListAdapter;
import dev.oneuiproject.oneuiexample.utils.DarkModeUtils;

public class MainActivity extends AppCompatActivity
        implements DrawerListAdapter.DrawerListener {
    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;
    private final List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initFragmentList();
        initDrawer();
        initFragments();

        mBinding.drawerLayout.post(() -> {
            TipPopup tipPopup = new TipPopup(mBinding.drawerLayout.getToolbar().getChildAt(0), TipPopup.MODE_TRANSLUCENT);
            tipPopup.setMessage("I'm Mr. Mee6, look at me!");
            tipPopup.setAction("Close", view -> {
            });
            //tipPopup.setExpanded(true);
            tipPopup.show(TipPopup.DIRECTION_BOTTOM_RIGHT);
        });
    }

    @Override
    public void attachBaseContext(Context context) {
        // pre-OneUI
        if (Build.VERSION.SDK_INT <= 28) {
            super.attachBaseContext(DarkModeUtils.createDarkModeContextWrapper(context));
        } else {
            super.attachBaseContext(context);
        }
    }

    private void initFragmentList() {
        fragments.add(new WidgetsFragment());
        fragments.add(new ProgressBarFragment());
        fragments.add(new SeekBarFragment());
        fragments.add(new SwipeRefreshFragment());
        fragments.add(new PreferencesFragment());
        fragments.add(null);
        fragments.add(new TabsFragment());
        fragments.add(null);
        fragments.add(new AppPickerFragment());
        fragments.add(new IndexScrollFragment());
        fragments.add(new PickersFragment());
        fragments.add(null);
        fragments.add(new QRCodeFragment());
        fragments.add(new IconsFragment());
    }

    @Override
    public void onBackPressed() {
        // Fix O memory leak
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O
                && isTaskRoot()
                && mFragmentManager.getBackStackEntryCount() == 0) {
            finishAfterTransition();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // pre-OneUI
        if (Build.VERSION.SDK_INT <= 28) {
            final Resources res = getResources();
            res.getConfiguration().setTo(DarkModeUtils.createDarkModeConfig(this, newConfig));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.sample3_menu_main, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about_app) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return false;
    }

    private void initDrawer() {
        mBinding.drawerLayout.setDrawerButtonIcon(getDrawable(R.drawable.ic_oui_info_outline));
        mBinding.drawerLayout.setDrawerButtonTooltip("About page");
        mBinding.drawerLayout.setDrawerButtonOnClickListener(v ->
                ActivityUtils.startPopOverActivity(this,
                        new Intent(MainActivity.this, SampleAboutActivity.class),
                        null,
                        ActivityUtils.POP_OVER_POSITION_TOP | ActivityUtils.POP_OVER_POSITION_CENTER_HORIZONTAL));

        mBinding.drawerListView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.drawerListView.setAdapter(new DrawerListAdapter(this, fragments, this));
        mBinding.drawerListView.setItemAnimator(null);
        mBinding.drawerListView.setHasFixedSize(true);
        mBinding.drawerListView.seslSetLastRoundedCorner(false);
    }

    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null) transaction.add(R.id.main_content, fragment);
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();

        onDrawerItemSelected(0);
    }

    @Override
    public boolean onDrawerItemSelected(int position) {
        Fragment newFragment = fragments.get(position);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentManager.getFragments()) {
            transaction.hide(fragment);
        }
        transaction.show(newFragment).commit();

        if (newFragment instanceof FragmentInfo) {
            if (!((FragmentInfo) newFragment).isAppBarEnabled()) {
                mBinding.drawerLayout.setExpanded(false, false);
                mBinding.drawerLayout.setExpandable(false);
            } else {
                mBinding.drawerLayout.setExpandable(true);
                mBinding.drawerLayout.setExpanded(false, false);
            }
            mBinding.drawerLayout.setTitle(getString(R.string.app_name), ((FragmentInfo) newFragment).getTitle());
            mBinding.drawerLayout.setExpandedSubtitle(((FragmentInfo) newFragment).getTitle());
        }
        mBinding.drawerLayout.setDrawerOpen(false, true);

        return true;
    }
}






sample-app/src/main/res/layout/activity_main.xml


<?xml version="1.0" encoding="utf-8"?>
<dev.oneuiproject.oneui.layout.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawerLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:expanded="false"
    app:title="@string/app_name">

    <FrameLayout
        android:id="@+id/main_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_location="main_content" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/drawer_list_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical"
        app:layout_location="drawer_panel" />

</dev.oneuiproject.oneui.layout.DrawerLayout>

