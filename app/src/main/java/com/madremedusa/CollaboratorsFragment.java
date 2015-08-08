package com.madremedusa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class CollaboratorsFragment extends Fragment {
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static CollaboratorsFragment newInstance() {
		CollaboratorsFragment fragment = new CollaboratorsFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public CollaboratorsFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_collaborators, container,false);
		final TextView text=(TextView)rootView.findViewById(R.id.text);
		final GridView colaboradores=(GridView)rootView.findViewById(R.id.colaboradores);
		text.setText(R.string.collaborator_text);
		colaboradores.setAdapter(new CollaboratorAdapter(getActivity()));

		return rootView;
	}
}
