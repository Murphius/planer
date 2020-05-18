package com.example.lkjhgf.recyclerView.detailedView;

import com.example.lkjhgf.helper.closeUp.CloseUpRecyclerView;

/**
 * Interface mit der Methode, die ausgeführt werden soll, wenn der Nutzer auf den Details anzeigen /
 * verbergen Button klickt <br/>
 * <p>
 *  Implementierung in {@link CloseUpRecyclerView}
 * </p>
 */
public interface OnItemClickListener {
    void onShowDetails(int position);
    void onOpenGoogleMaps(int position);
}
