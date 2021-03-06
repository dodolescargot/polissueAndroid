package ihm.si3.fr.unice.polytech.polissue.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.storage.StorageReference;

import java.util.Date;

/**
 * Class that models an issue
 */
public class IssueModel implements Parcelable{

    private String id;
    private String title;
    private String description;
    private Date date;
    private Emergency emergency;
    private Location location;
    private String userID;
    private String imagePath;
    private State state;


    public IssueModel() {
        // Default constructor required for calls to DataSnapshot.getValue(IssueModel.class)
    }

    public IssueModel(String id){
        this.id = id;
    }

    public IssueModel(String title, String description, Date date, Emergency emergency, Location location, String userID, String imagePath, State state) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.emergency = emergency;
        this.location = location;
        this.userID = userID;
        this.imagePath = imagePath;
        this.state=state;
    }

    public IssueModel(String title, String description, Date date, Emergency emergency, String userID, State state) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.emergency = emergency;
        this.userID = userID;
        this.state=state;
    }

    /**
     * Minimalistic constructor for an issue
     * @param id id in db
     * @param title the title of the issue
     * @param description the description of the issue
     * @param date the date of declaration of the issue
     * @param emergency the emergency level of the issue
     */
    public IssueModel(String id, String title, String description, long date, Emergency emergency, String userID, State state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = new Date(date);
        this.emergency = emergency;
        this.userID = userID;
        this.state=state;
    }

    /**
     * Full constructor for an issue
     * @param id id in db
     * @param title the title of the issue
     * @param description the description of the issue
     * @param date the date of declaration of the issue
     * @param emergency the emergency level of the issue
     * @param location the location of the issue
     * @param userID the userID who declared the issue
     * @param imagePath the image path on the firebase hosting bucket
     */
    public IssueModel(String id, String title, String description, Long date, Emergency emergency, Location location, String userID, String imagePath, State state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = new Date(date);
        this.emergency = emergency;
        this.location = location;
        this.userID = userID;
        this.imagePath = imagePath;
        this.state = state;
    }


    protected IssueModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        date = new Date(in.readLong());
        emergency = Emergency.valueOf(in.readString());
        location = in.readParcelable(Location.class.getClassLoader());
        userID =in.readString();
        imagePath = in.readString();
        state = State.valueOf(in.readString());
    }

    public static final Creator<IssueModel> CREATOR = new Creator<IssueModel>() {
        @Override
        public IssueModel createFromParcel(Parcel in) {
            return new IssueModel(in);
        }

        @Override
        public IssueModel[] newArray(int size) {
            return new IssueModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(date.getTime());
        dest.writeString(emergency.name());
        dest.writeParcelable(location, PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeString(userID);
        dest.writeString(imagePath);
        dest.writeString(state.name());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Long date) {
        if (date != null) this.date = new Date(date);
    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency emergency) {
        this.emergency = emergency;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void imagePathFromRef(StorageReference imageRef) {
        this.imagePath = imageRef.getPath();
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
