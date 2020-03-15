/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { MainDatabaseComponent } from '../../../../../../main/webapp/app/entities/main-database/main-database.component';
import { MainDatabaseService } from '../../../../../../main/webapp/app/entities/main-database/main-database.service';
import { MainDatabase } from '../../../../../../main/webapp/app/entities/main-database/main-database.model';

describe('Component Tests', () => {

    describe('MainDatabase Management Component', () => {
        let comp: MainDatabaseComponent;
        let fixture: ComponentFixture<MainDatabaseComponent>;
        let service: MainDatabaseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MainDatabaseComponent],
                providers: [
                    MainDatabaseService
                ]
            })
            .overrideTemplate(MainDatabaseComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MainDatabaseComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MainDatabaseService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MainDatabase(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.mainDatabases[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
