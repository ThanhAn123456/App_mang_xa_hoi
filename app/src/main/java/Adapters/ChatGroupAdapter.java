package Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatGroupAdapter extends RecyclerView.Adapter<ChatGroupAdapter.ChatGroupHolder> {
    @NonNull
    @Override
    public ChatGroupAdapter.ChatGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGroupAdapter.ChatGroupHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    class ChatGroupHolder extends RecyclerView.ViewHolder {
        public ChatGroupHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
