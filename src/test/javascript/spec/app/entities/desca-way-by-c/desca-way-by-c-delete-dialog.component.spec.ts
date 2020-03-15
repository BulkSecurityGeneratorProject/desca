/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { DescaWayByCDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/desca-way-by-c/desca-way-by-c-delete-dialog.component';
import { DescaWayByCService } from '../../../../../../main/webapp/app/entities/desca-way-by-c/desca-way-by-c.service';

describe('Component Tests', () => {

    describe('DescaWayByC Management Delete Component', () => {
        let comp: DescaWayByCDeleteDialogComponent;
        let fixture: ComponentFixture<DescaWayByCDeleteDialogComponent>;
        let service: DescaWayByCService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [DescaWayByCDeleteDialogComponent],
                providers: [
                    DescaWayByCService
                ]
            })
            .overrideTemplate(DescaWayByCDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DescaWayByCDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DescaWayByCService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
