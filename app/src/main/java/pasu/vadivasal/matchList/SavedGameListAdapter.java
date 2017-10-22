package pasu.vadivasal.matchList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pasu.vadivasal.R;
import pasu.vadivasal.globalModle.MatchDetails;


/**
 * Created by developer on 21/2/17.
 */


public class SavedGameListAdapter extends RecyclerView.Adapter {
    private FirebaseDatabase database;
    private Context context;
    private static final int PENDING_REMOVAL_TIMEOUT = 1500; // 3sec

    //    List<String> items;
//    List<String> itemsPendingRemoval;
    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn = true; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be
    List<MatchDetails> data = new ArrayList<>();


    boolean viewer;
    private MatchDetails onlineMatchID;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    private AlertDialog needSignUp;

//    public SavedGameListAdapter(Context context, RealmResults<MatchDetails> data) {
//        auth = FirebaseAuth.getInstance();
//        this.data = realm.copyFromRealm(data);
//        this.context = context;
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage(context.getString(R.string.processing));
//
//    }

    public SavedGameListAdapter(Context context, ArrayList<MatchDetails> data) {

        this.data = data;
        this.context = context;
        this.viewer = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

//        MatchShortSummaryData matchShortSummaryData = CommanData.fromJson(data.get(position).getmatchShortSummary(), MatchShortSummaryData.class);
//        TestViewHolder viewHolder = (TestViewHolder) holder;
//        // we need to show the "normal" state
//        viewHolder.itemView.setBackgroundColor(Color.WHITE);
//        viewHolder.titleView.setVisibility(View.VISIBLE);
//        // viewHolder.titleView.setText(item);
//        viewHolder.venue.setText(matchShortSummaryData.getLocation());
//        viewHolder.home_team_name.setText(matchShortSummaryData.getBattingTeamName());
//        viewHolder.away_team_name.setText(matchShortSummaryData.getBowlingTeamName());
//        viewHolder.status.setText(CommanData.getDateCurrentTimeZone(matchShortSummaryData.getTime()));
//        viewHolder.titleView.setTag(position);
//        if (viewer) {
//            viewHolder.online.setVisibility(View.GONE);
//            viewHolder.delete_item.setVisibility(View.INVISIBLE);
//            viewHolder.delete_item.setEnabled(false);
//        } else if (data.get(position).isOnlineMatch())
//            viewHolder.online.setImageResource(R.drawable.wifi_on);
//        else
//            viewHolder.online.setImageResource(R.drawable.wifi_off);
//
//        if (matchShortSummaryData.getStatus() == CommanData.MATCH_STARTED_FI) {
//
//
//            if (matchShortSummaryData != null) {
//                viewHolder.home_team_name.setText(matchShortSummaryData.getBattingTeamName());
//                viewHolder.away_team_name.setText(matchShortSummaryData.getBowlingTeamName());
//                viewHolder.home_team_scr.setText(matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
//                        matchShortSummaryData.getFirstInningsSummary().overs + ")");
//                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
//                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
//                viewHolder.away_team_scr.setVisibility(View.GONE);
//                viewHolder.status.setText(matchShortSummaryData.getQuotes());
//            }
//        } else if (matchShortSummaryData.getStatus() == CommanData.MATCH_STARTED_SI || matchShortSummaryData.getStatus() == CommanData.MATCH_COMPLETED) {
//
//
//            if (matchShortSummaryData != null) {
//                viewHolder.home_team_name.setText(matchShortSummaryData.getBowlingTeamName());
//                viewHolder.away_team_name.setText(matchShortSummaryData.getBattingTeamName());
//                viewHolder.home_team_scr.setText(matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
//                        matchShortSummaryData.getFirstInningsSummary().overs + ")");
//                viewHolder.away_team_scr.setText(matchShortSummaryData.getSecondInningsSummary().run + " - " + matchShortSummaryData.getSecondInningsSummary().wicket + " (" +
//                        matchShortSummaryData.getSecondInningsSummary().overs + ")");
//                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
//                viewHolder.away_team_scr.setVisibility(View.VISIBLE);
//                viewHolder.status.setText(matchShortSummaryData.getQuotes());
//            }
//        } else {
//
//            viewHolder.home_team_scr.setVisibility(View.GONE);
//            viewHolder.away_team_scr.setVisibility(View.GONE);
//        }


//        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void addData(ArrayList<MatchDetails> datas) {
//        System.out.println("md.getValue()$$" + datas.size() + "__" + data.size());
//        data.addAll(datas);
//        System.out.println("md.getValue()$" + datas.size() + "__" + data.size());
        notifyDataSetChanged();
    }


    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    class TestViewHolder extends RecyclerView.ViewHolder {

        ImageView
                home_team_image, away_team_image;
        TextView home_team_name;
        TextView away_team_name;
        TextView venue, status;
        TextView home_team_scr, away_team_scr;
        CardView titleView;
        AppCompatImageView delete_item;
        ImageView online, share;


        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_dashboard_match_item, parent, false));
//            titleView = (CardView) itemView.findViewById(R.id.titleView);
//            status = (TextView) itemView.findViewById(R.id.status);
//            home_team_image = (ImageView) itemView.findViewById(R.id.home_team_image);
//            home_team_name = (TextView) itemView.findViewById(R.id.home_team_name);
//            away_team_name = (TextView) itemView.findViewById(R.id.away_team_name);
//            venue = (TextView) itemView.findViewById(R.id.venue);
//            delete_item = (AppCompatImageView) itemView.findViewById(R.id.delete_item);
//            home_team_scr = (TextView) itemView.findViewById(R.id.home_team_scr);
//            away_team_scr = (TextView) itemView.findViewById(R.id.away_team_scr);
//            away_team_image = (ImageView) itemView.findViewById(R.id.away_team_image);
//            online = (ImageView) itemView.findViewById(R.id.online);
//            share = (ImageView) itemView.findViewById(R.id.share);
//            delete_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    remove(getAdapterPosition());
//                }
//            });
//            database = FirebaseDatabase.getInstance();
//
//            titleView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    System.out.println("__matachi_" + data.get(getAdapterPosition()).getMatch_id());
//
//                    if (!viewer) {
//                        MatchDetails md = RealmDB.getMatchById(context, realm, data.get(getAdapterPosition()).getMatch_id());
//                        if (md.getToss() == null) {
//                            Bundle b = new Bundle();
//                            MatchInfo fragment = new MatchInfo();
//                            b.putInt("match_id", md.getMatch_id());
//                            b.putString("venue", md.getLocation());
//                            b.putString("teamIDs", String.valueOf(md.getHomeTeam().team_id) + "__" + String.valueOf(md.getAwayTeam().team_id));
//                            fragment.setArguments(b);
//                            ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, fragment).commit();
//
//                        } else if (md.getMatchStatus() != CommanData.MATCH_COMPLETED) {
//                            if ((md.isOnlineMatch() && (auth.getCurrentUser() != null)) || !md.isOnlineMatch()) {
//                                Bundle b = new Bundle();
//                                MainActivity fragment = new MainActivity();
//                                b.putInt("match_id", md.getMatch_id());
//                                fragment.setArguments(b);
//                                ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, fragment).commit();
//                            } else
//                                showLoginAlert();
//                        } else {
//                            //Toast.makeText(context, context.getString(R.string.game_over), Toast.LENGTH_SHORT).show();
//                            Bundle b = new Bundle();
//                            MatchDetailActivity fragment = new MatchDetailActivity();
//                            b.putInt("match_id", md.getMatch_id());
//                            // Toast.makeText(context,  String.valueOf(md.getMatch_id()), Toast.LENGTH_SHORT).show();
//                            fragment.setArguments(b);
//                            ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, fragment).commit();
//                        }
//                    } else {
//                        //  MatchDetails matchDetails=data.get(getAdapterPosition());
//                        Bundle b = new Bundle();
//                        MatchDetailActivity fragment = new MatchDetailActivity();
//                        b.putInt("match_id", data.get(getAdapterPosition()).getMatch_id());
//                        b.putBoolean("is_online", true);
//                        b.putString("mss", data.get(getAdapterPosition()).getmatchShortSummary());
//                        fragment.setArguments(b);
//                        ((MainFragmentActivity) context).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainFrag, fragment).commit();
//                    }
//                }
//            });
//
//            share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MatchShortSummaryData matchShortSummaryData = CommanData.fromJson(data.get(getAdapterPosition()).getmatchShortSummary(), MatchShortSummaryData.class);
//                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    //intent.putExtra(Intent.EXTRA_STREAM, outputUri);
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    String shareContent = getShareContent(matchShortSummaryData);
//                    intent.putExtra(Intent.EXTRA_TEXT, shareContent);
//
////                    if (type == WHATSAPP) {
////                        intent.setPackage("com.whatsapp");
////                    } else if (type == FACEBOOK) {
////                        intent.setPackage("com.example.developer.fb");
////                    } else if (type == GMAIL) {
////                        intent.setPackage("com.google.android.gm");
////                    }
//
//
//                    //  intent.putExtra(Intent.EXTRA_TEXT, "Hai");
//                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    intent.setType("text/plain");
//                    context.startActivity(Intent.createChooser(intent, "Share image using"));
//                }
//            });
//            online.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (NetworkStatus.isOnline(context)) {
//                        if (auth.getCurrentUser() != null) {
//                            onlineMatchID = RealmDB.getMatchById(context, realm, data.get(getAdapterPosition()).getMatch_id());
//                            String type = "";
//                            if (onlineMatchID.getToss() == null)
//                                type = "upcoming";
//                            else if (onlineMatchID.getMatchStatus() != CommanData.MATCH_COMPLETED)
//                                type = "ongoing";
//                            else
//                                type = "recent";
//                            String s = "matchList/" + type + "/" + onlineMatchID.getMatch_id();
//                            System.out.println("________*" + onlineMatchID.getmatchShortSummary() + "__" + s);
//                            DatabaseReference myRef = database.getReference(s);
//                            progressDialog.show();
//                            if (onlineMatchID.isOnlineMatch()) {
//
//                                myRef.removeValue(new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                        progressDialog.cancel();
//                                        if (databaseError == null) {
//
//                                            realm.beginTransaction();
//                                            onlineMatchID.setOnlineMatch(false);
//                                            realm.commitTransaction();
//                                            online.setImageResource(R.drawable.wifi_off);
//                                        }
//                                    }
//                                });
//
//                            } else {
//
//
//                                myRef.setValue(CommanData.fromJson(onlineMatchID.getmatchShortSummary(), MatchShortSummaryData.class), new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                        progressDialog.cancel();
//                                        if (databaseError == null) {
//                                            realm.beginTransaction();
//                                            onlineMatchID.setOnlineMatch(true);
//                                            realm.commitTransaction();
//                                            online.setImageResource(R.drawable.wifi_on);
//                                        }
//                                    }
//                                });
//
//
//                            }
//                        } else {
//                            showLoginAlert();
//                        }
//                    } else {
//                        if (context instanceof MainFragmentActivity)
//                            ((MainFragmentActivity) context).showNetWorkAlert();
//                    }
//                }
//            });
//        }
//
//        public void remove(int position) {
//
//            int item = data.get(position).getMatch_id();
//
//            //  System.out.println("_______________II" + item + "___" + position);
//            realm.beginTransaction();
//            MatchDetails md = realm.where(MatchDetails.class).equalTo("match_id", data.get(position).getMatch_id()).findFirst();
//            RealmResults<InningsData> inningsDatas = realm.where(InningsData.class).equalTo("match_id", (item)).findAll();
//            RealmResults<BowlingProfile> bowlingProfiles = realm.where(BowlingProfile.class).equalTo("match_id", (item)).findAll();
//            RealmResults<BatingProfile> battingProfiles = realm.where(BatingProfile.class).equalTo("match_id", (item)).findAll();
//            if (md != null)
//                md.deleteFromRealm();
//            if (inningsDatas != null)
//                inningsDatas.deleteAllFromRealm();
//            if (bowlingProfiles != null)
//                bowlingProfiles.deleteAllFromRealm();
//            if (battingProfiles != null)
//                battingProfiles.deleteAllFromRealm();
//            realm.commitTransaction();
//            data.remove(position);
//            notifyItemRemoved(position);
//        }

        }

//    private String getShareContent(MatchShortSummaryData matchShortSummaryData) {
//        String shareContent = "" + matchShortSummaryData.getHomeTeam() + " " + context.getString(R.string.vs) + " " +
//                matchShortSummaryData.getAwayTeam() + "\n" + matchShortSummaryData.getLocation() + "\n";
//        if (matchShortSummaryData.getStatus() == CommanData.MATCH_STARTED_FI) {
//
//
//            if (matchShortSummaryData != null) {
//                shareContent += matchShortSummaryData.getBattingTeamName() + "    " + matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
//                        matchShortSummaryData.getFirstInningsSummary().overs + ")" + "\n" + matchShortSummaryData.getQuotes();
//
//
//            }
//        } else if (matchShortSummaryData.getStatus() == CommanData.MATCH_STARTED_SI || matchShortSummaryData.getStatus() == CommanData.MATCH_COMPLETED) {
//
//
//            if (matchShortSummaryData != null) {
//                shareContent += matchShortSummaryData.getBowlingTeamName() + "    " + matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
//                        matchShortSummaryData.getFirstInningsSummary().overs + ")"
//                        + "\n" + matchShortSummaryData.getBattingTeamName() + "  " + matchShortSummaryData.getSecondInningsSummary().run + " - " + matchShortSummaryData.getSecondInningsSummary().wicket + " (" +
//                        matchShortSummaryData.getSecondInningsSummary().overs + ")" + "\n" + matchShortSummaryData.getQuotes();
////                viewHolder.home_team_name.setText(matchShortSummaryData.getBowlingTeamName());
////                viewHolder.away_team_name.setText(matchShortSummaryData.getBattingTeamName());
////                viewHolder.home_team_scr.setText(matchShortSummaryData.getFirstInningsSummary().run + " - " + matchShortSummaryData.getFirstInningsSummary().wicket + " (" +
////                        matchShortSummaryData.getFirstInningsSummary().overs + ")");
////                viewHolder.away_team_scr.setText(matchShortSummaryData.getSecondInningsSummary().run + " - " + matchShortSummaryData.getSecondInningsSummary().wicket + " (" +
////                        matchShortSummaryData.getSecondInningsSummary().overs + ")");
////                viewHolder.home_team_scr.setVisibility(View.VISIBLE);
////                viewHolder.away_team_scr.setVisibility(View.VISIBLE);
////                viewHolder.status.setText(matchShortSummaryData.getQuotes());
//            }
//        }
//        return shareContent;
//    }


//    private void showLoginAlert() {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//        builder1.setMessage(context.getString(R.string.need_signup_online));
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                context.getString(R.string.ok),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        ((AppCompatActivity) context).startActivityForResult(new Intent(context, SocialLoginCustom.class), MainFragmentActivity.REQUEST_SIGN_UP);
//                    }
//                });
//        builder1.setNegativeButton(
//                context.getString(R.string.cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        needSignUp = builder1.create();
//
//
//        needSignUp.setOnShowListener(new android.content.DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(android.content.DialogInterface dialogs) {
//                needSignUp.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                needSignUp.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
//            }
//        });
//        needSignUp.show();
//    }

    }
}
