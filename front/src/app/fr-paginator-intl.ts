import { MatPaginatorIntl } from '@angular/material';

const frRangeLabel = (page: number, pageSize: number, length: number) => {
    if (length == 0 || pageSize == 0) { return `0 sur ${length}`; }
    
    length = Math.max(length, 0);
  
    const startIndex = page * pageSize;
  
    const endIndex = startIndex < length ?
        Math.min(startIndex + pageSize, length) :
        startIndex + pageSize;
  
    return `${startIndex + 1} - ${endIndex} sur ${length}`;
  }

export function getFrPaginatorIntl() {
    const paginatorIntl = new MatPaginatorIntl();
    
    paginatorIntl.itemsPerPageLabel = 'Lignes par page:';
    paginatorIntl.firstPageLabel = 'Première page';
    paginatorIntl.previousPageLabel = 'Page précédente';
    paginatorIntl.nextPageLabel = 'Page suivante';
    paginatorIntl.lastPageLabel = 'Dernière page';
    paginatorIntl.getRangeLabel = frRangeLabel;

    return paginatorIntl;
  }