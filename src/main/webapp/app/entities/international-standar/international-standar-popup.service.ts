import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { InternationalStandar } from './international-standar.model';
import { InternationalStandarService } from './international-standar.service';

@Injectable()
export class InternationalStandarPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private internationalStandarService: InternationalStandarService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.internationalStandarService.find(id)
                    .subscribe((internationalStandarResponse: HttpResponse<InternationalStandar>) => {
                        const internationalStandar: InternationalStandar = internationalStandarResponse.body;
                        this.ngbModalRef = this.internationalStandarModalRef(component, internationalStandar);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.internationalStandarModalRef(component, new InternationalStandar());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    internationalStandarModalRef(component: Component, internationalStandar: InternationalStandar): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.internationalStandar = internationalStandar;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
