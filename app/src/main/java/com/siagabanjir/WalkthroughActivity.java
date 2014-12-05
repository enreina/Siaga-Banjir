package com.siagabanjir;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flurry.android.FlurryAgent;

public class WalkthroughActivity extends ActionBarActivity {
	private ActionBar actionBar;
    private static final int MAX_VIEWS = 4;
    public Context context;

    ViewPager mViewPager;
    
    @Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.setReportLocation(true);
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.onStartSession(this, "CZWJXGNWJVHM35JYDTRC");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough_activity);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new WalkthroughPagerAdapter());
        mViewPager.setOnPageChangeListener(new WalkthroughPageChangeListener(this));
        
        actionBar = getSupportActionBar();
        actionBar.hide();
        
        Intent i = getIntent();
        
		//Flurry log
		FlurryAgent.logEvent("View_Walkthrough");
		
        //balikin ke aplikasinya bingung Rei hehe
        /*
        if (mViewPager.getAdapter().getCount() == MAX_VIEWS-1) {
        	Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
        */
    }


    class WalkthroughPagerAdapter extends PagerAdapter {
    	
        @Override
        public int getCount() {
            return MAX_VIEWS + 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View) object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            Log.e("walkthrough", "instantiateItem(" + position + ");");
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageViewContainer = inflater.inflate(R.layout.walkthrough_single_view, null);
            ImageView imageView = (ImageView) imageViewContainer.findViewById(R.id.image_view);

            switch(position) {
            case 0:
                imageView.setImageResource(R.drawable.image1);
                break;

            case 1:
                imageView.setImageResource(R.drawable.image2);
                break;

            case 2:
                imageView.setImageResource(R.drawable.image3);
                break;

            case 3:
                imageView.setImageResource(R.drawable.image4);
                break;
            case 4:
            	break;
            }

            ((ViewPager) container).addView(imageViewContainer, 0);
            return imageViewContainer;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView((View)object);
        }
    }


    class WalkthroughPageChangeListener implements ViewPager.OnPageChangeListener {
    	Context context; 
    	
    	public WalkthroughPageChangeListener(Context context) {
    		this.context = context;
    	}
    	
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        	if (arg0 == MAX_VIEWS) {
        		((Activity)context).finish();
        	}
        }

        @Override
        public void onPageSelected(int position) {
            // Here is where you should show change the view of page indicator
            switch(position) {

            case MAX_VIEWS - 1:
            	
                break;

            default:

            }

        }

    }
}