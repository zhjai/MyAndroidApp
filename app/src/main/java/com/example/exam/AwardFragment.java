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
import com.example.exam.data.TaskItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class AwardFragment extends Fragment implements SortModeListener {

    private RecyclerView recyclerView;
    private TextView emptyAwardTextView;
    private AwardAdapter awardAdapter;
    private AwardDataBank dataBank;
    private ArrayList<AwardItem> awardList = new ArrayList<>();
    private ArrayList<AwardItem> filteredAwardList = new ArrayList<>();
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
                    String awardGroup = data.getStringExtra("AWARD_GROUP");
                    Date date = (Date) data.getSerializableExtra("AWARD_DATE");
                    awardList = dataBank.loadObject();
                    for (AwardItem awardItem : awardList) {
                        if (awardItem.getName().equals(awardName)) {
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
                            builder.setTitle("添加失败");
                            builder.setMessage("奖励已存在");
                            builder.setPositiveButton("确定", (dialog, which) -> {});
                            builder.show();
                            return;
                        }
                    }
                    filteredAwardList.add(new AwardItem(awardName, awardPoints, awardGroup, date));
                    awardList.add(new AwardItem(awardName, awardPoints, awardGroup, date));
                    awardAdapter.notifyItemInserted(filteredAwardList.size() - 1);
                    dataBank.saveObject(awardList);
                    checkIfEmpty();
                    Snackbar.make(recyclerView, "添加一个奖励", Snackbar.LENGTH_LONG).show();
                }
            }
        );
        modifyAwardLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    // 这里是您处理返回结果的地方
                    Intent data = result.getData();
                    String oldAwardName = data.getStringExtra("OLD_AWARD_NAME");
                    String awardName = data.getStringExtra("AWARD_NAME");
                    int awardPoints = data.getIntExtra("AWARD_POINTS", -1);
                    String awardGroup = data.getStringExtra("AWARD_GROUP");
                    Date date = (Date) data.getSerializableExtra("AWARD_DATE");
                    int awardPosition = data.getIntExtra("AWARD_POSITION", -1);
                    awardList = dataBank.loadObject();
                    filteredAwardList.get(awardPosition).setName(awardName);
                    filteredAwardList.get(awardPosition).setPoints(awardPoints);
                    filteredAwardList.get(awardPosition).setGroup(awardGroup);
                    filteredAwardList.get(awardPosition).setDate(date);
                    modifyAward(oldAwardName, awardName, awardPoints, awardGroup, date);
                    setFilteredAwardList();
                    dataBank.saveObject(awardList);
                    Snackbar.make(recyclerView, "修改一个奖励", Snackbar.LENGTH_LONG).show();
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_award, container, false);
        TextView statusTextView = rootView.findViewById(R.id.award_status_text_view);
        recyclerView = rootView.findViewById(R.id.award_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBank = new AwardDataBank(getContext(), "awards");
        awardList = dataBank.loadObject();
        awardAdapter = new AwardAdapter(awardList, filteredAwardList, dataBank);
        recyclerView.setAdapter(awardAdapter);
        GlobalData.getCurrentGroup().observe(getViewLifecycleOwner(), t -> {
            awardList = dataBank.loadObject();
            setFilteredAwardList();
        });
        emptyAwardTextView = rootView.findViewById(R.id.empty_award_text_view);
        setFilteredAwardList();
        checkIfEmpty();

        AtomicReference<String> pointsText = new AtomicReference<>("");
        AtomicReference<String> groupText = new AtomicReference<>("");

        GlobalData.getPoints().observe(getViewLifecycleOwner(), points -> {
            TextView pointsTextView = rootView.findViewById(R.id.award_total_points);
            pointsTextView.setText(points.toString());
        });

        GlobalData.getCurrentGroup().observe(getViewLifecycleOwner(), group -> {
            statusTextView.setText("分组：" + group);
        });

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab_add_award);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddAwardActivity.class);
            addAwardLauncher.launch(intent);
        });

        GlobalData.getCurrentSortMode().observe(getViewLifecycleOwner(), currenSortMode -> {
            if (currenSortMode == "") return;
            awardList.sort(new Comparator<AwardItem>() {
                @Override
                public int compare(AwardItem o1, AwardItem o2) {
                    if (currenSortMode == "按照名称排序") return o1.getName().compareTo(o2.getName());
                    else if (currenSortMode == "按照日期排序") return o1.getDate().compareTo(o2.getDate());
                    else if (currenSortMode == "按照重要程度排序") return o2.getImportance() - o1.getImportance();
                    return 0;
                }
            });
            dataBank.saveObject(awardList);
            filteredAwardList.sort(new Comparator<AwardItem>() {
                @Override
                public int compare(AwardItem o1, AwardItem o2) {
                    if (currenSortMode == "按照名称排序") return o1.getName().compareTo(o2.getName());
                    else if (currenSortMode == "按照日期排序") return o1.getDate().compareTo(o2.getDate());
                    else if (currenSortMode == "按照重要程度排序") return o2.getImportance() - o1.getImportance();
                    return 0;
                }
            });
            awardAdapter.notifyDataSetChanged();
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
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
                builder.setTitle("删除奖励");
                builder.setMessage("你真的要删除奖励吗？");
                builder.setPositiveButton("是", (dialog, which) -> {
                    deleteAward(awardList.get(position).getName());
                    filteredAwardList.remove(position);
                    awardAdapter.notifyItemRemoved(position);
                    dataBank.saveObject(awardList);
                });
                builder.setNegativeButton("否", (dialog, which) -> {});
                builder.show();
                checkIfEmpty();
                Snackbar.make(recyclerView, "删除一个奖励", Snackbar.LENGTH_LONG).show();
                return true;
            case 2:
                intent = new Intent(getActivity(), ModifyAwardActivity.class);
                intent.putExtra("awardName", filteredAwardList.get(position).getName());
                intent.putExtra("awardPoints", filteredAwardList.get(position).getPoints());
                intent.putExtra("awardGroup", filteredAwardList.get(position).getGroup());
                intent.putExtra("awardDate", filteredAwardList.get(position).getDate());
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

    public void setFilteredAwardList() {
        filteredAwardList.clear();
        for (AwardItem awardItem : awardList) {
            if (GlobalData.currentGroup.getValue().equals("全部")) {
                filteredAwardList.add(awardItem);
            }
            else if (GlobalData.currentGroup.getValue().equals("未分组") && awardItem.getGroup() == null) {
                filteredAwardList.add(awardItem);
            }
            else if (GlobalData.currentGroup.getValue().equals(awardItem.getGroup())) {
                filteredAwardList.add(awardItem);
            }
        }
        awardAdapter.notifyDataSetChanged();
        checkIfEmpty();
    }

    public void deleteAward(String awardName) {
        for (int i = 0; i < awardList.size(); i++) {
            if (awardList.get(i).getName().equals(awardName)) {
                awardList.remove(i);
                break;
            }
        }
    }

    public void modifyAward(String oldAwardName, String awardName, int awardPoints, String awardGroup, Date date) {
        for (int i = 0; i < awardList.size(); i++) {
            if (awardList.get(i).getName().equals(oldAwardName)) {
                awardList.get(i).setName(awardName);
                awardList.get(i).setPoints(awardPoints);
                awardList.get(i).setGroup(awardGroup);
                awardList.get(i).setDate(date);
                break;
            }
        }
    }
}