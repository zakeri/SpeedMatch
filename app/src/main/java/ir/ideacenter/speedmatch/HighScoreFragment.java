package ir.ideacenter.speedmatch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreFragment extends Fragment {

    RecyclerView rankList;
    String gameName;

    public static HighScoreFragment newInstance(String gameName) {
        Bundle bundle = new Bundle();
        bundle.putString("game_name", gameName);

        HighScoreFragment fragment = new HighScoreFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);

        // read bundle arguments:
        Bundle bundle = getArguments();
        if (bundle != null) {
            gameName = bundle.getString("game_name");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setupRankList();
    }

    private void setupRankList() {
        HighScoreList hsl;
        if (gameName == getString(R.string.game_title_which_one))
            hsl = SharedPreferenceManager.getInstance(getActivity()).getWhichOneHighScoreList();
        else if (gameName == getString(R.string.game_title_speed_match))
            hsl = SharedPreferenceManager.getInstance(getActivity()).getSpeedMatchHighScoreList();
        else
            hsl = new HighScoreList();

        Comparator<HighScoreUser> userComparator = new Comparator<HighScoreUser>() {
            @Override
            public int compare(HighScoreUser o1, HighScoreUser o2) {
                if (o1.getHighScore() < o2.getHighScore())
                    return +1;
                else if (o1.getHighScore() > o2.getHighScore())
                    return -1;
                else
                    return 0;
            }
        };
        Collections.sort(hsl.getHighScoreUserList(), userComparator);

        HighScoreUserAdapter userAdapter = new HighScoreUserAdapter(hsl.getHighScoreUserList());
        rankList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankList.setAdapter(userAdapter);
    }

    private void findViews(View view) {
        rankList = (RecyclerView) view.findViewById(R.id.ranklist);
    }
}
