package com.example.exam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.data.AwardAdapter;
import com.example.exam.data.AwardDataBank;
import com.example.exam.data.AwardItem;
import com.example.exam.data.DragAndDropAwardCallback;
import com.example.exam.data.GlobalData;
import com.example.exam.data.SortModeListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AwardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AwardFragment extends Fragment implements SortModeListener {

    private RecyclerView recyclerView;
    private TextView emptyAwardTextView;
    private AwardAdapter awardAdapter;
    private AwardDataBank dataBank;
    private ArrayList<AwardItem> awardList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addAwardLauncher;
    private ActivityResultLauncher<Intent> modifyAwardLauncher;
    private boolean isCurrentFragment = false;
    private ItemTouchHelper itemTouchHelper;

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
                    for (AwardItem awardItem : awardList) {
                        if (awardItem.getName().equals(awardName)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                            builder.setTitle("添加失败");
                            builder.setMessage("奖励已存在");
                            builder.setPositiveButton("确定", (dialog, which) -> {});
                            builder.show();
                            return;
                        }
                    }
                    AwardItem newAward = new AwardItem(awardName, awardPoints);
                    awardList.add(newAward);
                    awardAdapter.notifyItemInserted(awardList.size() - 1);
                    dataBank.saveObject(awardList);
                    checkIfEmpty();
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
                    dataBank.saveObject(awardList);
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
        dataBank = new AwardDataBank(getContext(), "awards");
        awardList = dataBank.loadObject();
        awardAdapter = new AwardAdapter(awardList, dataBank);
        recyclerView.setAdapter(awardAdapter);
        emptyAwardTextView = rootView.findViewById(R.id.empty_award_text_view);
        checkIfEmpty();

        TextView pointsTextView = rootView.findViewById(R.id.award_points_text_view);
        GlobalData.getPoints().observe(getViewLifecycleOwner(), points -> {
            pointsTextView.setText("积分：" + points);
        });

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
        if (getActivity() instanceof MainActivity2) {
            ((MainActivity2) getActivity()).setCurrentSortModeListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isCurrentFragment = false;
        if (getActivity() instanceof MainActivity2) {
            ((MainActivity2) getActivity()).setCurrentSortModeListener(null);
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("删除奖励");
                builder.setMessage("你真的要删除奖励吗？");
                builder.setPositiveButton("是", (dialog, which) -> {
                    awardList.remove(position);
                    awardAdapter.notifyItemRemoved(position);
                    dataBank.saveObject(awardList);
                });
                builder.setNegativeButton("否", (dialog, which) -> {});
                builder.show();
                checkIfEmpty();
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

    public void checkIfEmpty() {
        if (awardList.isEmpty()) {
            emptyAwardTextView.setVisibility(View.VISIBLE);
        } else {
            emptyAwardTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEnterSortMode() {
        itemTouchHelper = new ItemTouchHelper(new DragAndDropAwardCallback(awardAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        awardAdapter.setContextMenuEnabled(false);
        awardAdapter.setSortMode(true);
    }

    @Override
    public void onExitSortMode() {
        itemTouchHelper.attachToRecyclerView(null);
        awardAdapter.setContextMenuEnabled(true);
        awardAdapter.setSortMode(false);
    }
}