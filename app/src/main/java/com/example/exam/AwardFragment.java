package com.example.exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.exam.data.AwardAdapter;
import com.example.exam.data.AwardItem;
import com.example.exam.data.TaskAdapter;
import com.example.exam.data.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AwardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AwardAdapter awardAdapter;
    private ArrayList<AwardItem> awardList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addAwardLauncher;
    private ActivityResultLauncher<Intent> modifyAwardLauncher;
    private boolean isCurrentFragment = false;

    public AwardFragment() {
        // Required empty public constructor
    }

    public static AwardFragment newInstance() {
        AwardFragment fragment = new AwardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAwardLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // 这里是您处理返回结果的地方
                    Intent data = result.getData();
                    String awardName = data.getStringExtra("AWARD_NAME");
                    int awardPoints = data.getIntExtra("AWARD_POINTS", -1);
                    AwardItem newAward = new AwardItem(awardName, awardPoints);
                    awardList.add(newAward);
                    awardAdapter.notifyItemInserted(awardList.size() - 1);
                }
            }
        );
        modifyAwardLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // 这里是您处理返回结果的地方
                    Intent data = result.getData();
                    String awardName = data.getStringExtra("AWARD_NAME");
                    int awardPoints = data.getIntExtra("AWARD_POINTS", -1);
                    int awardPosition = data.getIntExtra("AWARD_POSITION", -1);
                    awardList.get(awardPosition).setName(awardName);
                    awardList.get(awardPosition).setPoints(awardPoints);
                    awardAdapter.notifyItemChanged(awardPosition);
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_award, container, false);
        recyclerView = rootView.findViewById(R.id.award_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        awardList.add(new AwardItem("看手机", 300));
        awardList.add(new AwardItem("吃零食", 500));
        awardList.add(new AwardItem("看电脑", 1000));
        awardAdapter = new AwardAdapter(awardList);
        recyclerView.setAdapter(awardAdapter);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab_add_award);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddAwardActivity.class);
            addAwardLauncher.launch(intent);
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isCurrentFragment = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isCurrentFragment = false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (!isCurrentFragment) {
            return false;
        }
        int position = item.getOrder();
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(getActivity(), AddAwardActivity.class);
                addAwardLauncher.launch(intent);
                return true;
            case 1:
                awardList.remove(position);
                awardAdapter.notifyItemRemoved(position);
                return true;
            case 2:
                intent = new Intent(getActivity(), ModifyAwardActivity.class);
                intent.putExtra("awardName", awardList.get(position).getName());
                intent.putExtra("awardPoints", awardList.get(position).getPoints());
                intent.putExtra("awardPosition", position);
                modifyAwardLauncher.launch(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}