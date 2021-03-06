package ihm.si3.fr.unice.polytech.polissue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ihm.si3.fr.unice.polytech.polissue.R;
import ihm.si3.fr.unice.polytech.polissue.factory.IssueModelFactory;
import ihm.si3.fr.unice.polytech.polissue.fragment.IssueDetailFragment;
import ihm.si3.fr.unice.polytech.polissue.glide.GlideApp;
import ihm.si3.fr.unice.polytech.polissue.model.IssueModel;
import ihm.si3.fr.unice.polytech.polissue.model.State;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IssueModel}
 */
public class MyIssueRecyclerViewAdapter extends RecyclerView.Adapter<MyIssueRecyclerViewAdapter.ViewHolder> {

    private final List<IssueModel> mValues;
    private ChildEventListener issueEventListener;
    private DatabaseReference ref;
    private static final String TAG = "IssueViewAdapter";
    private Date date;


    public MyIssueRecyclerViewAdapter() {
        mValues = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("mishap");
        addEventListener();
        date = Calendar.getInstance().getTime();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_issue, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.issueModel = mValues.get(position);
        holder.issueTitle.setText(mValues.get(position).getTitle());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference declarerRef =  ref.child("users").child(holder.issueModel.getUserID()).child("username");
        declarerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.issueDeclarer.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        GlideApp.with(holder.mView)
                .load(holder.issueModel.getState().getDrawableId())
                .into(holder.issueState);
        holder.issueStateText.setText(holder.issueModel.getState().getMeaning());
        holder.issueDeclarer.setText(mValues.get(position).getUserID());
        long diff =date.getTime()- mValues.get(position).getDate().getTime();
        String expiredTime = calculateExpiredTime(diff, holder.mView.getContext());
        holder.issueDate.setText(expiredTime);
        if (mValues.get(position).getImagePath() != null && !mValues.get(position).getImagePath().isEmpty()) {
            StorageReference imageRef = FirebaseStorage.getInstance().getReference(mValues.get(position).getImagePath());
            GlideApp.with(holder.issueImage.getContext())
                    .load(imageRef)
                    .into(holder.issueImage);
        }else {
            GlideApp.with(holder.issueImage.getContext())
                    .load(R.mipmap.ic_logo_polissue)
                    .into(holder.issueImage);
        }

        holder.emergencyLight.setBackgroundResource(holder.issueModel.getEmergency().getDrawableID());


        holder.mView.setOnClickListener(v -> {
            FragmentTransaction ft = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
            Fragment issueDetailFragment = IssueDetailFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelable("issue", mValues.get(position));
            issueDetailFragment.setArguments(bundle);
            ft.replace(R.id.content_frame, issueDetailFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.commit();
        });
    }

    private String calculateExpiredTime(long diff, Context context) {
        double minutes = diff/1000/60;
        if ((int)minutes%10 > 0){
            double hours = minutes/60;
            if ((int)hours%10 > 0){
                double days = hours/24;
                if ((int)days%10>0){
                    double weeks = days/7;
                    if ((int)weeks%10>0){
                        double months = weeks/4;
                        if (months%10>0){
                            double years = months/12;
                            if ((int)years%10>0){
                                return String.valueOf((int)years)+context.getString(R.string.years);
                            }else {
                                return String.valueOf((int)months)+context.getString(R.string.months);
                            }
                        }else{
                            return String.valueOf((int)weeks)+context.getString(R.string.weeks);
                        }
                    }else{
                        return String.valueOf((int)days)+context.getString(R.string.days);
                    }
                }else {
                    return String.valueOf((int)hours)+context.getString(R.string.hours);
                }
            }else {
                return String.valueOf((int)minutes)+context.getString(R.string.minutes);
            }
        }else {
            return String.valueOf(diff/1000)+context.getString(R.string.seconds);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView issueImage;
        public final TextView issueTitle;
        public final TextView issueDeclarer;
        public final TextView issueDate;
        public final ImageView issueState;
        public final TextView issueStateText;
        public final View emergencyLight;
        public IssueModel issueModel;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            issueImage = view.findViewById(R.id.issueImage);
            issueTitle = view.findViewById(R.id.issueTitle);
            issueDeclarer = view.findViewById(R.id.issueDeclarer);
            issueDate = view.findViewById(R.id.issueDate);
            issueState = view.findViewById(R.id.issueState);
            issueStateText = view.findViewById(R.id.issueStateText);
            emergencyLight = view.findViewById(R.id.emergency_light);
        }
    }

    private void addEventListener() {
        issueEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                IssueModel issue = new IssueModelFactory().forge(dataSnapshot);
                mValues.add(issue);
                notifyItemInserted(mValues.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //TODO implement
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TODO implement
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO implement
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO implement
            }
        };
        ref.addChildEventListener(issueEventListener);
    }


}
