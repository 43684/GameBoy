import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gameboy.R
import com.example.gameboy.UserData

class HighScoreAdapter(private val highScores: List<UserData>) :
    RecyclerView.Adapter<HighScoreAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameTextView: TextView = itemView.findViewById(R.id.playerNameTextView)
        val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.high_score_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val highScore = highScores[position]
        holder.playerNameTextView.text = highScore.name
        holder.scoreTextView.text = highScore.highscore.toString()
    }

    override fun getItemCount(): Int {
        return highScores.size
    }
}
