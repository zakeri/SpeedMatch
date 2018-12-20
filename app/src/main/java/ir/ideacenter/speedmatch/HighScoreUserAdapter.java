package ir.ideacenter.speedmatch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HighScoreUserAdapter extends RecyclerView.Adapter<HighScoreUserAdapter.UserViewHolder> {

    List<HighScoreUser> items;

    public HighScoreUserAdapter(List<HighScoreUser> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ranklist_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        userViewHolder.name.setText(items.get(i).getUserName());
        userViewHolder.score.setText(String.valueOf(items.get(i).getHighScore()));
        userViewHolder.rank.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView score;
        TextView rank;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            score = itemView.findViewById(R.id.score);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}
