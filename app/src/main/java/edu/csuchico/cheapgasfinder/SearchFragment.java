package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SearchFragment extends ListFragment {

    ListView placesListView;
    List<se.walkercrou.places.Prediction> predictions;
    ArrayList<String> predictionNames;
    View view;

    private OnFragmentInteractionListener mListener;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final GooglePlaces client = new GooglePlaces("AIzaSyDi6lDOivgNS6g2zvaXJr6UqJoIvNXHDgA");

        SearchView user_search = (SearchView) view.findViewById(R.id.searchView);

        user_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    predictions = client.getPlacePredictions(s);

                    predictionNames = new ArrayList<String>();

                    for (se.walkercrou.places.Prediction prediction : predictions) {
                        predictionNames.add(prediction.toString());
                    }

                    placesListView = getListView();

                    ArrayAdapter<String> arrayAdapter
                            = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, predictionNames);
                    placesListView.setAdapter(arrayAdapter);
                }
                return true;
            }
        });

        //places = client.getPlacesByQuery("Chevron", GooglePlaces.MAXIMUM_RESULTS);

        //placeNames = new ArrayList<String>();

        /*
        predictionNames = new ArrayList<String>();

        for (se.walkercrou.places.Prediction prediction: predictions) {
            predictionNames.add(prediction.toString());
        }

        for (Place place: places) {
            placeNames.add(place.getName());
        }
        */

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        placesListView = getListView();

        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, predictionNames);
        placesListView.setAdapter(arrayAdapter);
        */
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
