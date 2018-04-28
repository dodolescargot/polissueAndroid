package ihm.si3.fr.unice.polytech.polissue.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ihm.si3.fr.unice.polytech.polissue.adapter.MyIssueRecyclerViewAdapter;
import ihm.si3.fr.unice.polytech.polissue.R;
import ihm.si3.fr.unice.polytech.polissue.model.IssueModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class IssueListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private List<IssueModel> issues;
    private DatabaseReference ref;
    private ChildEventListener issueEventListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IssueListFragment() {
        issues = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("mishap");
    }


    public static IssueListFragment newInstance(int columnCount) {
        IssueListFragment fragment = new IssueListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            addEventListener();
            recyclerView.setAdapter(new MyIssueRecyclerViewAdapter(issues));
        }
        return view;
    }

    private void addEventListener(){
        issueEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                IssueModel issue = dataSnapshot.getValue(IssueModel.class);
                issues.add(issue);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ref.removeEventListener(issueEventListener);
    }
}
