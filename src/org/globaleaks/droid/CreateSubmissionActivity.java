package org.globaleaks.droid;

import org.globaleaks.droid.ReceiversFragment.OnContextSelectedListener;
import org.globaleaks.droid.R;
import org.globaleaks.util.Logger;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class CreateSubmissionActivity extends FragmentActivity 
implements ActionBar.TabListener, OnContextSelectedListener
{

	SectionsPagerAdapter pagerAdapter;
	ViewPager pager;
    
    public static final int REQUEST_SELECT_IMG    = 0;
    public static final int REQUEST_TAKE_IMG      = 1;
	public final static int REQUEST_STORE_RECEIPT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLApplication app = (GLApplication) getApplication();
		if(savedInstanceState == null) {
			// new activity => reset globale state
			app.resetSubmission();
		}
		
		setContentView(R.layout.activity_create_submission);
		setTitle(R.string.title_activity_new_submission);
		findViewById(android.R.id.home).setPadding(10, 1, 10, 1);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);

		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
		    Tab tab = actionBar.newTab();
		    tab.setText(pagerAdapter.getPageTitle(getResources(),i))
		       .setTabListener(this);
			actionBar.addTab(tab);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.submission, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_submit:
			    GLApplication app = (GLApplication) getApplication();
			    new CreateSubmissionTask(this).execute(app);
			    return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Fragment[] fragments = new Fragment[3];

		public SectionsPagerAdapter(FragmentManager fm) {			
			super(fm);
			fragments[0] = new ReceiversFragment();
			fragments[1] = new FieldsFragment();
			fragments[2] = new AttachmentsFragment();
			android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.pager, fragments[0]);
			ft.add(R.id.pager, fragments[1]);
			ft.add(R.id.pager, fragments[2]);
			//ft.commit();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments[position];		
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		public CharSequence getPageTitle(Resources res, int position) {
			if(position >= 0 && position < fragments.length) {
				return ((SubmissionFragment)fragments[position]).getTitle(res);
			}
			return null;
		}

	}

	@Override
	public void onContextSelected() {
		FieldsFragment ff = (FieldsFragment) pagerAdapter.fragments[1];
		ff.refreshFields();
	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		if(request == REQUEST_STORE_RECEIPT) {
			Logger.i("Got back");
			finish();
		} else {
			super.onActivityResult(request, result, data);
		}
	}
}
