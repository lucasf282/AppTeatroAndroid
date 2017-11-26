package br.com.appteatro.appteatro.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.appteatro.appteatro.R;
import br.com.appteatro.appteatro.fragement.EventoFragment;
import br.com.appteatro.appteatro.fragement.ImportFragment;
import br.com.appteatro.appteatro.fragement.PerfilFragment;

public class TabsAdapter extends FragmentPagerAdapter {
    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.eventos);
        } else if (position == 1) {
            return context.getString(R.string.destaque);
        }
        return context.getString(R.string.favoritos);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = EventoFragment.newInstance(R.string.eventos);
        } else if (position == 1) {
            f = ImportFragment.newInstance(R.string.destaque);
        } else {
            f = PerfilFragment.newInstance(R.string.favoritos);
        }
        return f;
    }
}

