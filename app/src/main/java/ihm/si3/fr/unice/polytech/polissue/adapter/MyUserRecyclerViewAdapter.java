package ihm.si3.fr.unice.polytech.polissue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import ihm.si3.fr.unice.polytech.polissue.R;
import ihm.si3.fr.unice.polytech.polissue.fragment.UserListFragment;
import ihm.si3.fr.unice.polytech.polissue.model.IssueModel;
import ihm.si3.fr.unice.polytech.polissue.model.User;

/**
 * {@link RecyclerView.Adapter} that can display a {@link User}
 * TODO: Replace the implementation with code for your data type.
 */
public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private UserListFragment.OnCheckUserListener checkUserListener;



    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(holder.mItem.getUsername());

        holder.mView.setOnClickListener(v -> {
            holder.notify.setChecked(!holder.notify.isChecked());
            if (holder.notify.isChecked()){
                checkUserListener.onUserChecked(holder.mItem);
            }else {
                checkUserListener.onUserUnchecked(holder.mItem);
            }
        });
        holder.notify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                checkUserListener.onUserChecked(holder.mItem);
            }else {
                checkUserListener.onUserUnchecked(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void filter(List<User> users) {
        mValues = users;
        notifyDataSetChanged();;
    }


    public void setCheckUserListener(UserListFragment.OnCheckUserListener checkUserListener) {
        this.checkUserListener = checkUserListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final CheckBox notify;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = view.findViewById(R.id.name);
            notify = view.findViewById(R.id.notifyCheckBox);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
