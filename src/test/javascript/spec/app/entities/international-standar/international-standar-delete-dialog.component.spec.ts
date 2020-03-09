/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DescaTestModule } from '../../../test.module';
import { InternationalStandarDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/international-standar/international-standar-delete-dialog.component';
import { InternationalStandarService } from '../../../../../../main/webapp/app/entities/international-standar/international-standar.service';

describe('Component Tests', () => {

    describe('InternationalStandar Management Delete Component', () => {
        let comp: InternationalStandarDeleteDialogComponent;
        let fixture: ComponentFixture<InternationalStandarDeleteDialogComponent>;
        let service: InternationalStandarService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [InternationalStandarDeleteDialogComponent],
                providers: [
                    InternationalStandarService
                ]
            })
            .overrideTemplate(InternationalStandarDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternationalStandarDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternationalStandarService);
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
