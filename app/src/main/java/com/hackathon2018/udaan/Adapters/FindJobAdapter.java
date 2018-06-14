package com.hackathon2018.udaan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackathon2018.udaan.Models.FindJobs;
import com.hackathon2018.udaan.Models.News;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aniketvishal on 31/03/18.
 */

public class FindJobAdapter extends RecyclerView.Adapter<FindJobAdapter.FindJobsViewHolder> {

    public List<FindJobs> JobsList;
    private Context context;

    public FindJobAdapter(List<FindJobs> jobsList, Context context) {
        JobsList = jobsList;
        this.context = context;
    }

    @Override
    public FindJobsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_jobs_layout, parent, false);
        return new FindJobsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FindJobsViewHolder holder, int position) {

        final Activity activity = (Activity)context;

        holder.getNewsData(JobsList.get(position).getTitle(),
                JobsList.get(position).getDesc(),
                JobsList.get(position).getSector(),
                JobsList.get(position).getSalary());

        holder.mFjlApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"Thank You for appling, we will get back to you shortly",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return JobsList.size();
    }

    public class FindJobsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView mFjlTitle,mFjlDesc,mFjlSector,mFjlSalary;
        public Button mFjlApplyBtn;

        public FindJobsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mFjlTitle = (TextView)mView.findViewById(R.id.fjl_title);
            mFjlDesc = (TextView)mView.findViewById(R.id.fjl_desc);
            mFjlSector = (TextView)mView.findViewById(R.id.fjl_sector);
            mFjlSalary = (TextView)mView.findViewById(R.id.fjl_salary);

            mFjlApplyBtn = (Button) mView.findViewById(R.id.fjl_apply_btn);

        }

        public void getNewsData(String title, String desc,String sector,String salary){

            mFjlTitle.setText(title);
            mFjlDesc.setText(desc);
            mFjlSector.setText(sector);
            mFjlSalary.setText(salary);

        }
    }

}
