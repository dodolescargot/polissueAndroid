package ihm.si3.fr.unice.polytech.polissue.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

import ihm.si3.fr.unice.polytech.polissue.DataBaseAccess;
import ihm.si3.fr.unice.polytech.polissue.IncidentLocalisationActivity;
import ihm.si3.fr.unice.polytech.polissue.R;
import ihm.si3.fr.unice.polytech.polissue.model.Emergency;
import ihm.si3.fr.unice.polytech.polissue.model.IssueModel;
import ihm.si3.fr.unice.polytech.polissue.model.Location;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeclareIssueFragment extends Fragment{

    private static final int REQUEST_GET_MAP_LOCATION = 0;
    private static final String TAG = "DeclareIssueFragment";
    private ImageButton validButton, addImage, currentLocation, cancelButton;
    private ImageView image;
    private EditText title, description, declarer, location;
    private SeekBar emergencyLevel;
    private TextView titleError, declarerError, locationError;

    private Location locationMap;


    public DeclareIssueFragment() {

    }


    public static Fragment newInstance(){
        return new DeclareIssueFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_declare_issue, container, false);
        validButton = view.findViewById(R.id.validButton);
        addImage = view.findViewById(R.id.addImageButton);
        currentLocation = view.findViewById(R.id.currentLocationButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        image = view.findViewById(R.id.issueImagePreview);
        title = view.findViewById(R.id.titleTextField);
        description = view.findViewById(R.id.descriptionTextField);
        declarer = view.findViewById(R.id.declarerTextField);
        location = view.findViewById(R.id.locationTextField);
        emergencyLevel = view.findViewById(R.id.emergencyLevel);
        titleError = view.findViewById(R.id.titleError);
        declarerError = view.findViewById(R.id.declarerError);
        locationError = view.findViewById(R.id.locationError);

        validButton.setOnClickListener((v) -> {
            if(checkMandatoryFields()){
                IssueModel issue = new IssueModel(title.getText().toString(),description.getText().toString(),new Date(), Emergency.MEDIUM,locationMap,declarer.getText().toString(),"http://www.picslyrics.net/images/141613-rick-astley-never-gonna-give-you-up.jpg");
                DataBaseAccess dataBaseAccess = new DataBaseAccess();
                dataBaseAccess.postIssue(issue);
            }
        });

        cancelButton.setOnClickListener((v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, IssueListFragment.newInstance(2));
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }));

        addImage.setOnClickListener(v -> {
            //TODO implement adding an image
        });

        currentLocation.setOnClickListener(v -> {
            Intent localisationActivity=new Intent(this.getActivity(), IncidentLocalisationActivity.class);
            startActivityForResult(localisationActivity,REQUEST_GET_MAP_LOCATION);
        });

        return view;
    }

    private boolean checkMandatoryFields() {
        boolean ok = true;
        titleError.setVisibility(View.GONE);
        declarerError.setVisibility(View.GONE);
        locationError.setVisibility(View.GONE);

        if (title.getText().length() == 0 || title.getText().toString().equals("")){
            titleError.setVisibility(View.VISIBLE);
            ok = false;
        }
        if (declarer.getText().length() == 0 || declarer.getText().toString().equals("")){
            declarerError.setVisibility(View.VISIBLE);
            ok=false;
        }
        if (location.getText().length() == 0 || location.getText().toString().equals("")){
            locationError.setVisibility(View.VISIBLE);
            ok = false;
        }
        return ok;
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GET_MAP_LOCATION && resultCode == Activity.RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            this.locationMap=new Location(location.getText().toString(),longitude,latitude);
            // do something with B's return values
        }
    }
}